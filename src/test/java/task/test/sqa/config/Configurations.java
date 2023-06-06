package task.test.sqa.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:configuration.properties"
})
public interface Configurations extends Config {

    String url();
}
