package net.andrecarbajal.minecontrolfx.download;

import net.andrecarbajal.minecontrolfx.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public interface IDownloader {
    default void download(URL url, Path path) throws IOException {
        URLConnection connection = url.openConnection();
        int fileSize = connection.getContentLength();

        try (ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
             FileOutputStream fos = new FileOutputStream(path.toFile())) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            int bytesRead;
            int totalBytesRead = 0;
            while ((bytesRead = rbc.read(buffer)) != -1) {
                buffer.flip();
                fos.getChannel().write(buffer);
                buffer.clear();

                totalBytesRead += bytesRead;
                double progress = (double) totalBytesRead / fileSize * 100;
            }
        } catch (IOException e) {
            Constants.LOGGER.error("Error downloading file: {}", String.valueOf(e));
        }
    }
}
