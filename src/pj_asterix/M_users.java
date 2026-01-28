package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_users {

    private Db_mariadb db;

    private int id;
    private String name;
    private String email;
    private String password;
    private String commentaire;
    private int roleId;

    /* =====================
       CONSTRUCTEURS
       ===================== */

    // Constructeur simple (utilisé dans getRecords)
    public M_users(Db_mariadb db, int id, String name, String email, String password, String commentaire, int roleId) {
        this.db = db;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.commentaire = commentaire;
        this.roleId = roleId;
    }

    // Constructeur INSERT
    public M_users(Db_mariadb db, String name, String email, String password, String commentaire, int roleId) throws SQLException {
        this.db = db;
        this.name = name;
        this.email = email;
        this.password = password;
        this.commentaire = commentaire;
        this.roleId = roleId;

        String comSql = (commentaire == null)
                ? "NULL"
                : "'" + commentaire + "'";

        String sql;
        sql = "INSERT INTO mcd_users(name, email, password, commentaire, role_id) VALUES ("
                + "'" + name + "', "
                + "'" + email + "', "
                + "'" + password + "', "
                + comSql + ", "
                + roleId + ");";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();
    }

    // Constructeur SELECT par id
    public M_users(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql;
        sql = "SELECT * FROM mcd_users WHERE id = " + id + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.name = res.getString("name");
        this.email = res.getString("email");
        this.password = res.getString("password");
        this.commentaire = res.getString("commentaire");
        this.roleId = res.getInt("role_id");

        res.close();
    }

    /* =====================
       GETTERS / SETTERS
       ===================== */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /* =====================
       CRUD
       ===================== */

    public void update() throws SQLException {

        String comSql = (commentaire == null)
                ? "NULL"
                : "'" + commentaire + "'";

        String sql;
        sql = "UPDATE mcd_users SET "
                + "name = '" + name + "', "
                + "email = '" + email + "', "
                + "password = '" + password + "', "
                + "commentaire = " + comSql + ", "
                + "role_id = " + roleId + " "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    public void delete(int id) throws SQLException {
        String sql;
        sql = "DELETE FROM mcd_users WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* =====================
       GET RECORDS
       ===================== */

    public static LinkedHashMap<Integer, M_users> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_users> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Integer, M_users> lesUtilisateurs = new LinkedHashMap<>();

        String sql;
        sql = "SELECT * FROM mcd_users WHERE " + clauseWhere + " ORDER BY name;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");
            String name = res.getString("name");
            String email = res.getString("email");
            String password = res.getString("password");
            String commentaire = res.getString("commentaire");
            int roleId = res.getInt("role_id");

            M_users unUtilisateur =
                    new M_users(db, id, name, email, password, commentaire, roleId);

            lesUtilisateurs.put(id, unUtilisateur);
        }

        res.close();
        return lesUtilisateurs;
    }

    /* =====================
       CONNEXION
       ===================== */

    public static M_users connexion_log(Db_mariadb db, String email, String motPasse) throws SQLException {

        String sql;
        sql = "SELECT id FROM mcd_users WHERE "
                + "email = '" + email + "' "
                + "AND password = '" + motPasse + "';";

        ResultSet res = db.sqlSelect(sql);

        if (res.first()) {
            int id = res.getInt("id");
            res.close();
            return new M_users(db, id);
        }

        res.close();
        return null;
    }

    /* =====================
       AFFICHAGE
       ===================== */

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }

    /* =====================
       MAIN DE TEST
       ===================== */

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase =
                new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);

        LinkedHashMap<Integer, M_users> lesUtilisateurs =
                M_users.getRecords(mabase);

        for (Integer uneCle : lesUtilisateurs.keySet()) {
            M_users u = lesUtilisateurs.get(uneCle);
            System.out.println(u);
        }
    }
}
