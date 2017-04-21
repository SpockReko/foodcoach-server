package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.avaje.datasource.DataSourceConfig;

import java.io.File;

/**
 * Created by fredrikkindstrom on 2017-03-23.
 */
public class CommonTools {

    public static EbeanServer getDatabase() {
        Config conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
        DataSourceConfig foodDB = new DataSourceConfig();
        ServerConfig config = new ServerConfig();

        config.setName("mysql");
        foodDB.setDriver("com.mysql.jdbc.Driver");
        foodDB.setUsername(conf.getString("db.default.username"));
        foodDB.setPassword(conf.getString("db.default.password"));
        foodDB.setUrl(conf.getString("db.default.url"));
        config.setDataSourceConfig(foodDB);
        config.setDefaultServer(true);
        config.setRegister(false);

        return EbeanServerFactory.create(config);
    }
}
