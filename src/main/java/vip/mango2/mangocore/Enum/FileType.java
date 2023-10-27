package vip.mango2.mangocore.Enum;

import com.alibaba.fastjson2.JSON;
import vip.mango2.mangocore.Entity.File.MangoFile;
import vip.mango2.mangocore.Entity.File.MangoJsonFile;
import vip.mango2.mangocore.Entity.File.MangoYamlFile;

public enum FileType {
    YAML {
        @Override
        public MangoFile createFile(String path) {
            return new MangoYamlFile(path);
        }
    },

    JSON {
        @Override
        public MangoFile createFile(String path) {
            return new MangoJsonFile(path);
        }
    };

    public abstract MangoFile createFile(String path);
}
