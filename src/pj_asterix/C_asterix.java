package pj_asterix;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class C_asterix {
    private Db_mariadb baseRR;
    private M_Adorer adorer;
    private M_Album album;
    private M_Apparaitre apparaitre;
    private M_personnages unPersonnage;  // ← CHANGÉ : M_Personnage → M_personnages
    private M_categorie uneCategorie;
    private M_citations uneCitations;
    private M_citer unCiter;
    private M_divinites uneDivinite;
    private M_exercer unExerce;
    private M_genres unGenre;
    private M_role unRole;
    private M_users unUser;
    
    private V_main fm_main;
    private V_Personnage fm_personnage;
    private V_Divinite fm_divinite;
    
    Integer uneCle;
    
    private LinkedHashMap <Integer, M_role> lesRoles;
    private LinkedHashMap <Integer, M_users> lesUsers;
    private LinkedHashMap <Integer, M_personnages> lesPersonnages;
    private LinkedHashMap <Integer, M_peuples> lesPeuples;
    private LinkedHashMap <Integer, M_divinites> lesDivinites;
    
    public C_asterix() throws Exception{
        connection();
        fm_main = new V_main(this);
        fm_personnage = new V_Personnage(fm_main, true);
        fm_divinite = new V_Divinite(fm_main,true);
        fm_main.afficher();
    }
    
    private void connection () throws Exception{
        baseRR = new Db_mariadb(CL_Connection.url, CL_Connection.login , CL_Connection.password);
    }
    
    public void deconnexion() throws SQLException {
        baseRR.closeBase();
    }
    
    public void aff_V_Personnage() throws SQLException{
        lesPersonnages = M_personnages.getRecords(baseRR);
        lesPeuples = M_peuples.getRecords(baseRR);
        fm_personnage.afficher(this, lesPersonnages, lesPeuples);
    }
    
    public void enregistrer(int idPerso, String nom, String com, char genre, int peuple) throws IOException, SQLException{
        unPersonnage = new M_personnages(baseRR, idPerso);  // ← CHANGÉ
        
        unPersonnage.setNom(nom);
        unPersonnage.setCommentaire(com);
        unPersonnage.setGenreCode(String.valueOf(genre));  // ← CHANGÉ : char → String
        unPersonnage.setPeupleId(peuple);
        
        unPersonnage.update();
        aff_V_Personnage();
    }
    
    public void ajouter(String nom, String com, char genre, int peuple) throws IOException, SQLException{
        // ← CHANGÉ : M_Personnage → M_personnages + conversion char→String
        unPersonnage = new M_personnages(baseRR, nom, com, String.valueOf(genre), peuple);
        lesPersonnages.put(unPersonnage.getId(), unPersonnage);  // ← CHANGÉ : uneCle → getId()
        aff_V_Personnage();
    }
    
    public void supp_Personnage(int iPerso) throws SQLException{
        unPersonnage = new M_personnages(baseRR, iPerso);  // ← CHANGÉ
        unPersonnage.delete();  // ← CHANGÉ : pas de paramètre
        aff_V_Personnage();
    }
    public void aff_V_Divinite() throws SQLException{
        lesDivinites = M_divinites.getRecords(baseRR);
        fm_divinite.afficher(this, lesDivinites);
    }
    
   public void enregistrer_divi(int idDivi, String nom, String con, String com) throws IOException, SQLException{
    uneDivinite = new M_divinites(baseRR, idDivi);  // ✅ idDivi (avec 'd')
    uneDivinite.setNom(nom);
    uneDivinite.setCommentaire(com);
    uneDivinite.setContexte(con);
    
    uneDivinite.update();
    aff_V_Divinite();
}
    
    public void ajouter_divi(String nom, String com, String con) throws IOException, SQLException{
        // ← CHANGÉ : M_Personnage → M_personnages + conversion char→String
        uneDivinite = new M_divinites(baseRR, nom, com, con);
        lesDivinites.put(uneDivinite.getId(), uneDivinite);  // ← CHANGÉ : uneCle → getId()
        aff_V_Personnage();
    }
    
    public void supp_divi(int iDivi) throws SQLException{
        uneDivinite = new M_divinites(baseRR, iDivi);  // ← CHANGÉ
        uneDivinite.delete();  // ← CHANGÉ : pas de paramètre
        aff_V_Personnage();
    }
    
    public static void main(String[] args) throws Exception {
        C_asterix leControler = new C_asterix();
    }
   
    public Db_mariadb getBaseRR() {
        return baseRR;
    }
}