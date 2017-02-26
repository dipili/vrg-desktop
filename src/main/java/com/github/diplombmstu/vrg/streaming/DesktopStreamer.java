package com.github.diplombmstu.vrg.streaming;

import org.jitsi.impl.neomedia.imgstreaming.DesktopInteractImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class DesktopStreamer
{
    private static final Logger LOGGER = Logger.getLogger(DesktopStreamer.class.getName());

    private static final long OPEN_CAMERA_POLL_INTERVAL_MS = 1000L;
    private static DesktopInteractImpl desktopInteract;
    private final Object mLock = new Object();
    private final MovingAverage mAverageSpf = new MovingAverage(50);
    private final int mPort;
    private final int mPreviewSizeIndex;
    private final int mJpegQuality;
    private boolean mRunning = false;
    private int mPreviewFormat = Integer.MIN_VALUE;
    private int mPreviewWidth = Integer.MIN_VALUE;
    private int mPreviewHeight = Integer.MIN_VALUE;
    private int mPreviewBufferSize = Integer.MIN_VALUE;
    private MemoryOutputStream mJpegOutputStream = null;
    private MJpegHttpStreamer mMJpegHttpStreamer = null;
    private long mNumFrames = 0L;
    private long mLastTimestamp = Long.MIN_VALUE;

    public DesktopStreamer(final int port, final int previewSizeIndex, final int jpegQuality) throws AWTException
    {
        super();

        desktopInteract = new DesktopInteractImpl();

        mPort = port;
        mPreviewSizeIndex = previewSizeIndex;
        mJpegQuality = jpegQuality;
    }

    public void start()
    {
        synchronized (mLock)
        {
            if (mRunning)
            {
                throw new IllegalStateException("DesktopStreamer is already running");
            }
            mRunning = true;
        }

        tryStartStreaming();

        Thread thread = new Thread(() ->
                                   {
                                       //noinspection InfiniteLoopStatement
                                       while (true)
                                       {
                                           try
                                           {
                                               ImageIO.write(desktopInteract.captureScreen(),
                                                             "jpeg",
                                                             mJpegOutputStream);
                                           }
                                           catch (IOException e)
                                           {
                                               e.printStackTrace(); // TODO
                                           }

                                           sendPreviewFrame(new Date().getTime());
                                       }
                                   });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Stop the image streamer. The camera will be released during the
     * execution of stop() or shortly after it returns. stop() should
     * be called on the main thread.
     */
    void stop()
    {
        synchronized (mLock)
        {
            if (!mRunning)
            {
                throw new IllegalStateException("DesktopStreamer is already stopped");
            }

            mRunning = false;
            if (mMJpegHttpStreamer != null)
            {
                mMJpegHttpStreamer.stop();
            }
        }
    }

    private void tryStartStreaming()
    {
        try
        {
            while (true)
            {
                try
                {
                    startStreamingIfRunning();
                }
                catch (final RuntimeException openCameraFailed)
                {
                    LOGGER.config("Open camera failed, retying in " + OPEN_CAMERA_POLL_INTERVAL_MS + "ms");
                    Thread.sleep(OPEN_CAMERA_POLL_INTERVAL_MS);
                    continue;
                }
                break;
            }
        }
        catch (final Exception startPreviewFailed)
        {
            // Captures the IOException from startStreamingIfRunning and
            // the InterruptException from Thread.sleep.
            LOGGER.warning("Failed to start camera preview");
        }
    }

    private void startStreamingIfRunning() throws IOException
    {
        // Set up preview callback
        mPreviewWidth = 100; // TODO
        mPreviewHeight = 100;
        final int BITS_PER_BYTE = 8;
        final int bytesPerPixel = 100 / BITS_PER_BYTE;
        // XXX: According to the documentation the buffer size can be
        // calculated by width * height * bytesPerPixel. However, this
        // returned an error saying it was too small. It always needed
        // to be exactly 1.5 times larger.
        mPreviewBufferSize = mPreviewWidth * mPreviewHeight * bytesPerPixel * 3 / 2 + 1;

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

    private void sendPreviewFrame(final long timestamp)
    {
        // Calcalute the timestamp
        final long MILLI_PER_SECOND = 1000L;
        final long timestampSeconds = timestamp / MILLI_PER_SECOND;

        // Update and log the frame rate
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

//        // Create JPEG
//        BufferedImage image = new BufferedImage(mPreviewFormat, mPreviewWidth, BufferedImage.TYPE_INT_ARGB);
//        final YuvImage image = new YuvImage(data, mPreviewFormat, mPreviewWidth, mPreviewHeight, null);
//        image.compressToJpeg(mPreviewRect, mJpegQuality, mJpegOutputStream);

        mMJpegHttpStreamer.streamJpeg(mJpegOutputStream.getBuffer(), mJpegOutputStream.getLength(), timestamp);

        // Clean up
        mJpegOutputStream.seek(0);
    }
}

