import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import org.apache.commons.lang.WordUtils;

public class Venue {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Venue(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherVenue){
    if (!(otherVenue instanceof Venue)) {
      return false;
    } else {
      Venue newVenue = (Venue) otherVenue;
      return this.getName().equals(newVenue.getName()) &&
             this.getId() == newVenue.getId();
    }
  }

  public static List<Venue> all() {
    String sql = "SELECT * FROM venues";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Venue.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO venues (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Venue find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues where id=:id";
      Venue venue = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Venue.class);
      return venue;
    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE venues SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void firstToUppercase() {
    this.name = WordUtils.capitalize(this.name.toLowerCase());
  }

  public void addBand(Band band) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id)";
      con.createQuery(sql)
        .addParameter("band_id", band.getId())
        .addParameter("venue_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Band> getBands() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT band_id FROM bands_venues WHERE venue_id = :venue_id";
      List<Integer> bandIds = con.createQuery(sql)
        .addParameter("venue_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Band> bands = new ArrayList<Band>();

      for (Integer bandId : bandIds) {
          String venueQuery = "Select * From bands WHERE id = :bandId";
          Band band = con.createQuery(venueQuery)
            .addParameter("bandId", bandId)
            .executeAndFetchFirst(Band.class);
          bands.add(band);
      }
      return bands;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM venues WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM bands_venues WHERE venue_id = :venueId";
        con.createQuery(joinDeleteQuery)
          .addParameter("venueId", this.getId())
          .executeUpdate();
    }
  }

  public void deleteAll() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM venues;";
        con.createQuery(deleteQuery)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM bands_venues WHERE venue_id > 0";
        con.createQuery(joinDeleteQuery)
          .executeUpdate();
    }
  }
}
