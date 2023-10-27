package vip.mango2.mangocore.Entity.File;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class MangoYamlFile extends MangoFile {

        private YamlConfiguration yamlConfig;


        public MangoYamlFile(String filePath) {
            super(filePath);
            this.yamlConfig = new YamlConfiguration();
        }

        @Override
        public void load() throws IOException {
            try {
                yamlConfig.load(file);
                loaded = true;
            } catch (Exception e) {
                throw new IOException("Yaml文件加载失败，文件名称 [" + file.getName() + "]", e);
            }
        }

        @Override
        public void save() throws IOException {
            yamlConfig.save(file);
        }

        @Override
        public Object get(String path) {
            return yamlConfig.get(path);
        }

        @Override
        public void set(String path, Object value) {
            yamlConfig.set(path, value);
        }

    public void loadFromString(String string) throws InvalidConfigurationException {
        yamlConfig.loadFromString(string);
    }
}
