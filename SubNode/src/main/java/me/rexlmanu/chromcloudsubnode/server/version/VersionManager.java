package me.rexlmanu.chromcloudsubnode.server.version;

import me.rexlmanu.chromcloudcore.server.defaults.Version;
import me.rexlmanu.chromcloudcore.utility.web.UrlUtils;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.logging.Level;

public final class VersionManager {

    private File versionCache;

    public VersionManager() {
        this.versionCache = new File("ChromCloud/versions/");
        if(!this.versionCache.exists()){
            this.versionCache.mkdir();
        }
    }

    public File getVersion(Version version) {
        File jarLocation = new File(versionCache, version.getId() + "-" + version.getVersion() + "-" + version.getType() + ".jar");
        if (!jarLocation.exists()) {
            //Dont exist in the cache
            try {
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.WARNING, "Downloading version with the id " + version.getId() + " to the cache.");
                this.download(Objects.requireNonNull(UrlUtils.getAsUrl(version.getJarDownload())), jarLocation);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return jarLocation;
    }

    private void download(URL url, File file) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
