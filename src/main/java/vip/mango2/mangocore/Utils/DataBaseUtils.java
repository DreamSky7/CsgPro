package vip.mango2.mangocore.Utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.Plugin;
import vip.mango2.mangocore.Entity.DataBaseConfig;
import vip.mango2.mangocore.MangoCore;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataBaseUtils {

    private static DruidDataSource druidDataSource;

    private Plugin plugin;

    public DataBaseUtils(Plugin plugin, DataBaseConfig dataBaseConfig) {
        try {
            plugin = this.plugin;
            druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            // 链接池相关参数


            druidDataSource.setUrl("jdbc:mysql://"+dataBaseConfig.getConnect() +":"+ dataBaseConfig.getPort() +"/" + dataBaseConfig.getDataBase() +"?useSSL=false");
            druidDataSource.setUsername(dataBaseConfig.getUser());
            druidDataSource.setPassword(dataBaseConfig.getPassword());
            // 初始连接数
            druidDataSource.setInitialSize(5);
            // 最小空闲连接数
            druidDataSource.setMinIdle(0);
            // 最大空闲连接数
            druidDataSource.setMaxActive(20);
            // 连接的最长等待时间
            druidDataSource.setMaxWait(15000);
        } catch (Exception e) {
            MessageUtils.consoleMessage("&d[MangoCore] &c数据库连接失败");
            e.printStackTrace();
        }
    }

    public Connection getConnection () throws SQLException {
        return druidDataSource.getConnection();
    }

    /**
     * 释放资源
     * @param connection 连接
     * @param preparedStatement 预编译
     * @param resultSet 结果集
     */
    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除某个表
     * @param tableName 表名
     * @param condition 条件
     * @return 返回删除的行数
     * @throws Exception 异常
     */
    public int delete(String tableName, String condition) throws Exception{
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(tableName);
        if(StringUtils.isNotBlank(condition)) {
            sql.append(" where ").append(condition); // 条件
        }
        // try-with-resources 语句自动释放资源
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 更新数据
     * @param tableName 表名
     * @param column 列名
     * @param obj 数据对象
     * @param condition 条件
     * @return 返回更新的行数
     * @param <T> 泛型
     * @throws Exception 异常
     */
    public <T> int update(String tableName, List<String> column, T obj, String condition) throws Exception{
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        Map<String, Object> mapValue = new ConcurrentHashMap<>();

        // 获取对象的所有字段
        Class<?> tClass = obj.getClass();
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // 判断是否有指定的列名
            if(column != null && !column.isEmpty()) {
                // 如果有指定的列名，判断是否包含
                if(column.contains(declaredField.getName())) {
                    if(declaredField.get(obj) != null) {
                        mapValue.put(declaredField.getName(), declaredField.get(obj));
                    }
                }
            } else {
                // 如果没有指定列名，判断是否为空
                if(declaredField.get(obj) != null) {
                    mapValue.put(declaredField.getName(), declaredField.get(obj));
                }
            }
        }
        // 循环更新字段
        sql.append(mapValue.keySet().stream().map(item ->"`" + item + "`=?").collect(Collectors.joining(",")));
        if(StringUtils.isNotBlank(condition)) {
            sql.append(" where ").append(condition); // 条件
        }

        // try-with-resources 语句自动释放资源
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            int num = 1;
            // 循环设置参数
            for (String s : mapValue.keySet()) {
                preparedStatement.setObject(num, mapValue.get(s));
                num ++;
            }
            // 执行更新
            rows = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 插入数据
     * @param tableName 表名
     * @param column 字段
     * @param obj 数据对象
     * @return 返回插入的行数
     */
    public <T> int insert(String tableName,List<String> column, T obj) throws Exception{
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);

        Map<String, Object> mapValue = new ConcurrentHashMap<>();
        // 获取对象的所有字段
        Class<?> tClass = obj.getClass();
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // 判断是否有指定的列名
            if(column != null && !column.isEmpty()) {
                // 如果有指定的列名，判断是否包含
                if(column.contains(declaredField.getName())) {
                    if(declaredField.get(obj) != null) {
                        mapValue.put(declaredField.getName(), declaredField.get(obj));
                    }
                }
            } else {
                // 如果没有指定列名，判断是否为空
                if(declaredField.get(obj) != null) {
                    mapValue.put(declaredField.getName(), declaredField.get(obj));
                }

            }
        }
        sql.append(mapValue.keySet().stream().map(item ->"`" + item + "`").collect(Collectors.joining(",", "(", ")")));
        sql.append(" VALUES ");
        sql.append(mapValue.keySet().stream().map(item -> "?").collect(Collectors.joining(",","(",")")));
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            int num = 1;
            // 循环设置参数
            for (String s : mapValue.keySet()) {
                preparedStatement.setObject(num, mapValue.get(s));
                num ++;
            }
            // 执行更新
            rows =  preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 查询数量方法
     * @param tableName 表名
     * @param condition 条件
     * @return 返回查询的数量
     */
    public int selectCount(String tableName, String condition) {
        int result = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(0) as count ");
        sql.append(" FROM ");
        sql.append(tableName); // 表名
        sql.append(" ");
        if(StringUtils.isNotBlank(condition)) {
            sql.append(" where ").append(condition); // 条件
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询方法
     * @param tableName 查询表格
     * @param column 字段
     * @param condition 附加查询条件（需原生SQL）
     * @param tClass 数据对象
     */
    public <T> List<T> select(String tableName, String column,String condition,Class<T> tClass) throws Exception{

        List<T> t = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if(StringUtils.isNotBlank(column)) {
            sql.append(column);
        } else {
            sql.append("*");
        }
        sql.append(" FROM ");
        sql.append(tableName); // 表名
        sql.append(" ");
        if(StringUtils.isNotBlank(condition)) {
            sql.append(" where ").append(condition); // 条件
        }

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery()) {
            // 获取对象的所有字段
            while (resultSet.next()) {
                T obj = tClass.newInstance();
                Field[] declaredFields = tClass.getDeclaredFields();
                for (Field field: declaredFields) {
                    field.setAccessible(true);
                    try {
                        // 判断是否有指定的列名
                        if(isExistColumn(field.getName(), resultSet)) {
                            field.set(obj, resultSet.getObject(field.getName()));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                t.add(obj);
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 判断是否存在字段
     * @param name 字段名
     * @param resultSet 结果集
     * @return 返回是否存在
     */
    public boolean isExistColumn(String name, ResultSet resultSet) {
        try {
            resultSet.findColumn(name);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

}
