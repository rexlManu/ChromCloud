package me.rexlmanu.chromcloudnode.database;

import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.server.defaults.ServerConfiguration;
import me.rexlmanu.chromcloudcore.server.defaults.ServerMode;
import me.rexlmanu.chromcloudcore.server.defaults.Version;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseHandler {

    public static Server getServerById(int id) {
        try {
            final PreparedStatement prepare = ChromCloudNode.getInstance().getDatabaseManager().prepare("SELECT * FROM servers WHERE id = ?;");
            prepare.setInt(1, id);
            final ResultSet resultSet = ChromCloudNode.getInstance().getDatabaseManager().getMySQL().query(prepare);
            if (resultSet.next()) {
                return new Server(id, DatabaseHandler.getVersionById(resultSet.getInt("version")), new ServerConfiguration(resultSet.getInt("max_players"), resultSet.getString("motd"), ServerMode.valueOf(resultSet.getString("mode").toUpperCase()), resultSet.getInt("ram"), resultSet.getInt("port"), resultSet.getString("ftp_password"), resultSet.getString("lastSubnode")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Version getVersionById(int id) {
        try {
            final PreparedStatement prepare = ChromCloudNode.getInstance().getDatabaseManager().prepare("SELECT * FROM versions WHERE id = ?;");
            prepare.setInt(1, id);
            final ResultSet resultSet = ChromCloudNode.getInstance().getDatabaseManager().getMySQL().query(prepare);
            if (resultSet.next()) {
                return new Version(id, resultSet.getString("jar_name"), resultSet.getString("jar_download"), resultSet.getBoolean("ftb_modpack"), resultSet.getBoolean("legacyjavafixer"), resultSet.getString("version"), resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
