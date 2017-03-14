package com.github.diplombmstu.mixing;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.*;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormat;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ReceiveApplication extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        LibJitsi.start();
        MediaService mediaService = LibJitsi.getMediaService();

        MediaDevice device = mediaService.getDefaultDevice(MediaType.AUDIO, MediaUseCase.CALL);

        MediaStream mediaStream = mediaService.createMediaStream(device);
        mediaStream.setDirection(MediaDirection.RECVONLY);

        MediaFormat format = mediaService.getFormatFactory().createMediaFormat("PCMU");
        mediaStream.setFormat(format);

        mediaStream.setConnector(new DefaultStreamConnector(new DatagramSocket(8554), new DatagramSocket(8555)));

        mediaStream.setTarget(new MediaStreamTarget(new InetSocketAddress("192.168.1.112", 8551),
                                                    new InetSocketAddress("192.168.1.112", 8552)));

        mediaStream.start();
    }
}
