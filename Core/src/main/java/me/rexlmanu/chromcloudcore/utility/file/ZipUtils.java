package me.rexlmanu.chromcloudcore.utility.file;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    public static void zip(File inputFolder, File targetZippedFolder) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(targetZippedFolder);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        if (inputFolder.isFile())
            zipFile(inputFolder, "", zipOutputStream);
        else if (inputFolder.isDirectory())
            zipFolder(zipOutputStream, inputFolder, "");
        zipOutputStream.close();
    }

    public static void zipFolder(ZipOutputStream zipOutputStream, File inputFolder, String parentName) throws IOException {
        String myname = parentName + inputFolder.getName() + "\\";
        ZipEntry folderZipEntry = new ZipEntry(myname);
        zipOutputStream.putNextEntry(folderZipEntry);
        File[] contents = inputFolder.listFiles();
        assert contents != null;
        for (File f : contents) {
            if (f.isFile())
                zipFile(f, myname, zipOutputStream);
            else if (f.isDirectory())
                zipFolder(zipOutputStream, f, myname);
        }
        zipOutputStream.closeEntry();
    }

    private static void zipFile(File inputFile, String parentName, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(parentName + inputFile.getName());
        zipOutputStream.putNextEntry(zipEntry);
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, bytesRead);
        }
        zipOutputStream.closeEntry();
    }

    public static void extractFolder(File zipFile, String extractFolder) {
        try {
            int BUFFER = 2048;

            ZipFile zip = new ZipFile(zipFile);
            new File(extractFolder).mkdir();
            Enumeration zipFileEntries = zip.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(extractFolder, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zip
                            .getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,
                            BUFFER);
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
        }
    }

}
