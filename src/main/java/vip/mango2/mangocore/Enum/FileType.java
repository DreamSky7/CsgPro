package vip.mango2.mangocore.Enum;

import vip.mango2.mangocore.Entity.File.MangoConfiguration;
import vip.mango2.mangocore.Entity.File.MangoFile;
import vip.mango2.mangocore.Entity.File.Configuration.MangoJsonFile;
import vip.mango2.mangocore.Entity.File.Configuration.MangoYamlFile;

public enum FileType {
    YAML {
        @Override
        public MangoConfiguration createFile(String path) {
            return new MangoYamlFile(path);
        }
    },

    JSON {
        @Override
        public MangoConfiguration createFile(String path) {
            return new MangoJsonFile(path);
        }
    };

    public abstract MangoConfiguration createFile(String path);
}
