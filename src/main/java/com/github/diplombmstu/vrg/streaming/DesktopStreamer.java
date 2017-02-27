package com.github.diplombmstu.vrg.streaming;

import com.github.diplombmstu.vrg.common.ExceptionUtils;
import org.jitsi.impl.neomedia.device.ScreenDeviceImpl;
import org.jitsi.impl.neomedia.imgstreaming.DesktopInteractImpl;
import org.jitsi.service.neomedia.device.ScreenDevice;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class DesktopStreamer
{
    private static final Logger LOGGER = Logger.getLogger(DesktopStreamer.class.getName());

    private static DesktopInteractImpl desktopInteract;
    private final Object mLock = new Object();
    private final MovingAverage mAverageSpf = new MovingAverage(50);
    private final int mPort;
    private boolean mRunning = false;
    private MemoryOutputStream mJpegOutputStream = null;
    private MJpegHttpStreamer mMJpegHttpStreamer = null;
    private long mNumFrames = 0L;
    private long mLastTimestamp = Long.MIN_VALUE;
    private Thread frameLoopThread;

    public DesktopStreamer(final int port) throws AWTException
    {
        super();

        desktopInteract = new DesktopInteractImpl();

        mPort = port;
    }

    public void start() throws InterruptedException
    {
        LOGGER.info("Starting desktop streamer...");

        synchronized (mLock)
        {
            if (mRunning)
            {
                throw new IllegalStateException("DesktopStreamer is already running");
            }
            mRunning = true;
        }

        startJpegStreamer();

        //noinspection InfiniteLoopStatement
        frameLoopThread = new Thread(() ->
                                     {
                                         //noinspection InfiniteLoopStatement
                                         while (true)
                                         {
                                             sendNextFrame(new Date().getTime());
                                         }
                                     });
        frameLoopThread.start();

        LOGGER.info("Desktop streaming has been successfully started.");
    }

    /**
     * Stop the image streamer. The camera will be released during the
     * execution of stop() or shortly after it returns. stop() should
     * be called on the main thread.
     */
    public void stop()
    {
        LOGGER.info("Stopping dekstop streamer...");

        synchronized (mLock)
        {
            if (!mRunning)
            {
                throw new IllegalStateException("DesktopStreamer is already stopped");
            }

            frameLoopThread.stop();
            mRunning = false;
            if (mMJpegHttpStreamer != null)
            {
                mMJpegHttpStreamer.stop();
            }
        }
    }

    private void startJpegStreamer() throws InterruptedException
    {
        ScreenDevice screenDevice = ScreenDeviceImpl.getDefaultScreenDevice();

        int mPreviewWidth = screenDevice.getSize().width;
        int mPreviewHeight = screenDevice.getSize().height;
        final int bitsPerByte = 8;
        final double bytesPerPixel = 1.38 * bitsPerByte; // TODO magic num

        // XXX: According to the documentation the buffer size can be
        // calculated by width * height * bytesPerPixel. However, this
        // returned an error saying it was too small. It always needed
        // to be exactly 1.5 times larger.
        int mPreviewBufferSize = (int) (mPreviewWidth * mPreviewHeight * bytesPerPixel * 1.5 + 1);

        // We assumed that the compressed image will be no bigger than
        // the uncompressed image.
        mJpegOutputStream = new MemoryOutputStream(mPreviewBufferSize);

        final MJpegHttpStreamer streamer = new MJpegHttpStreamer(mPort, mPreviewBufferSize);
        streamer.start();

        synchronized (mLock)
        {
            if (!mRunning)
            {
                streamer.stop();
                return;
            }

            mMJpegHttpStreamer = streamer;
        }
    }

    private void sendNextFrame(final long timestamp)
    {
        try
        {
            ImageIO.write(desktopInteract.captureScreen(), "jpeg", mJpegOutputStream);
        }
        catch (IOException e)
        {
            LOGGER.severe(ExceptionUtils.buildStackTrace(e));
        }

        final long MILLI_PER_SECOND = 1000L;
        final long timestampSeconds = timestamp / MILLI_PER_SECOND;

        final long LOGS_PER_FRAME = 10L;

        mNumFrames++;
        if (mLastTimestamp != Long.MIN_VALUE)
        {
            mAverageSpf.update(timestampSeconds - mLastTimestamp);
            if (mNumFrames % LOGS_PER_FRAME == LOGS_PER_FRAME - 1)
            {
                LOGGER.config("FPS: " + 1.0 / mAverageSpf.getAverage());
            }
        }

        mLastTimestamp = timestampSeconds;

        mMJpegHttpStreamer.streamJpeg(mJpegOutputStream.getBuffer(), mJpegOutputStream.getLength(), timestamp);

        mJpegOutputStream.seek(0);
    }
}

