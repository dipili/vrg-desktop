package com.github.diplombmstu.vrg.screenstreaming;

import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.*;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormat;
import org.jitsi.service.neomedia.format.MediaFormatFactory;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * TODO add comment
 */
public class ScreenStreamer
{
    public void start() throws SocketException
    {
        LibJitsi.start();

        MediaService mediaService = LibJitsi.getMediaService();

        int localPort = 12345;
        int remotePort = 12349;

        MediaType mediaType = MediaType.VIDEO;

        // TODO use get devices
        MediaDevice device = mediaService.getDefaultDevice(mediaType, MediaUseCase.DESKTOP);

        MediaStream mediaStream = mediaService.createMediaStream(device);
        mediaStream.setDirection(MediaDirection.SENDONLY);

        String encoding = "H264";
        double clockRate = MediaFormatFactory.CLOCK_RATE_NOT_SPECIFIED;
        byte dynamicRTPPayloadType = 99;

        MediaFormat format = mediaService.getFormatFactory().createMediaFormat(encoding, clockRate);

        mediaStream.addDynamicRTPPayloadType(dynamicRTPPayloadType, format);

        mediaStream.setFormat(format);

        int localRTPPort = localPort;
        int localRTCPPort = localPort + 1;

        StreamConnector connector = new DefaultStreamConnector(new DatagramSocket(localRTPPort),
                                                               new DatagramSocket(localRTCPPort));

        mediaStream.setConnector(connector);

        int remoteRTPPort = remotePort;
        int remoteRTCPPort = remotePort + 1;

        mediaStream.setTarget(new MediaStreamTarget(new InetSocketAddress("localhost", remoteRTPPort),
                                                    new InetSocketAddress("localhost", remoteRTCPPort)));

        mediaStream.setName(mediaType.toString());

        mediaStream.start();
    }
}
