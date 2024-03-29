package Ejercicio_1;

import Ejercicio_2.departamento;
import Ejercicio_2.empleado;

import java.sql.*;

public class ejercicio1 {
    private static Connection conexion = null;

    public static void main(String[] args) {
        ejercicio1.connect();

        // a) Añade una consulta para visualizar nombre y número de departamento, y empleados del mismo
        infoDepartamento();

        // b) DatabaseMetaData
        ejercicio1.printDBinfo();

        // c) Insertar empleado
        insertarEmpleado(Integer.parseInt(args[0]), args[1], args[2], Integer.parseInt(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]), Integer.parseInt(args[7]));

        ejercicio1.disconnect();
    }

    public static void connect() {
        try {
            // Establecer la conexión con la base de datos
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/unidad2", "instituto", "");
        } catch (SQLException e) {
            System.err.println("Error al establecer la conexión con la base de datos: " + e);
        }
    }

    public static void disconnect() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión con la base de datos: " + e);
        }
    }

    public static void infoDepartamento() {
        try {
            Statement sentencia = conexion.createStatement();
            String sql = "Select * from departamentos";
            ResultSet resul = sentencia.executeQuery(sql);
            while (resul.next()) {
                departamento dep = new departamento(resul.getInt(1), resul.getString(2), resul.getString(3));
                System.out.println("Departamento nº: " + dep.getDept_no());
                System.out.println("Nombre: " + dep.getDnombre());
                System.out.println("Localidad: " + dep.getLoc());

                Statement empleados = conexion.createStatement();
                String query = "Select * from empleados where dept_no = " + dep.getDept_no();
                ResultSet resultSet = empleados.executeQuery(query);

                while (resultSet.next()) {
                    empleado emp = new empleado(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getDate(5), resultSet.getFloat(6), resultSet.getFloat(7), resultSet.getInt(8));
                    System.out.println(emp.toString());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e);
        }
    }

    public static void printDBinfo() {
        try {
            DatabaseMetaData m = conexion.getMetaData();
            System.out.println("INFORMACION SOBRE LA BASE DE DATOS");
            System.out.println("==================================");
            System.out.println("Nombre: " + m.getDatabaseProductName());
            System.out.println("Driver: " + m.getDriverName());
            System.out.println("URL: " + m.getURL());
            System.out.println("Usuario: " + m.getUserName());
            ResultSet resultSet = m.getTables("unidad2", null, null, null);
            while (resultSet.next()) {
                System.out.printf("Tabla %s %n", resultSet.getString("TABLE_NAME").toUpperCase());
                System.out.printf("\tTipo: %s %n", resultSet.getString("TABLE_TYPE"));
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e);
        }
    }

    public static void insertarEmpleado(int n_empleado, String apellido, String oficio, int dir, float salario, float comision, int n_dep) {
        try {
            String sql = "Insert into empleados values (" + n_empleado + ",'" + apellido + "','" + oficio + "'," + dir + ", now()," + salario + "," + comision + "," + n_dep + ")";
            PreparedStatement insertar = conexion.prepareStatement(sql);
            insertar.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar el empleado: " + e);
        }
    }
}
