package com.foodfetish.picker;

import java.sql.*;
import java.util.Locale;
import java.util.Properties;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        try {
//           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_example", "root", "4405");
//            System.out.println(connection.getCatalog());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("ok");
        String url = "jdbc:mysql://localhost:3306/db_example";
        Properties prop = new Properties();
        prop.put("user", "springuser");
        prop.put("password", "ThePassword");
        prop.put("autoReconnect", "true");
        prop.put("characterEncoding", "UTF-8");
        prop.put("useUnicode", "true");
        try (
            Connection connection = DriverManager.getConnection(url , prop );
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){
            String sql = "SELECT name, calories, prots, fats, carbs, id FROM recipe";
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuilder builder = new StringBuilder();
            while(resultSet.next()){

                String name = resultSet.getString(1);
                int cals = resultSet.getInt("calories");
                int calors =(int)(resultSet.getDouble(3)*4.1+ resultSet.getDouble(4)*9.3+ resultSet.getDouble(5)*4.1);
                builder.append(resultSet.getLong("id") + "\t" + name.toUpperCase(Locale.ROOT) + "\t" + cals + "\t" + calors + "\n");
            }
            System.out.println(builder);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
