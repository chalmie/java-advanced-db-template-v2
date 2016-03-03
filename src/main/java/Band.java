import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import org.apache.commons.lang.WordUtils;

public class Band {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Band(String name) {
    this.name = name;
  }

  public static List<Band> all() {
    String sql = "SELECT * FROM bands";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Band.class);
    }
  }

  @Override
  public boolean equals(Object otherBand){
    if (!(otherBand instanceof Band)) {
      return false;
    } else {
      Band newBand = (Band) otherBand;
      return this.getName().equals(newBand.getName());
    }
  }

  public void firstToUppercase() {
    this.name = WordUtils.capitalize(this.name.toLowerCase());
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM bands WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM bands_venues WHERE band_id = :bandId";
        con.createQuery(joinDeleteQuery)
          .addParameter("bandId", this.getId())
          .executeUpdate();
    }
  }

  public void deleteAll() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM bands;";
        con.createQuery(deleteQuery)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM bands_venues WHERE band_id > 0";
        con.createQuery(joinDeleteQuery)
          .executeUpdate();
    }
  }

  public static Band find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands where id=:id";
      Band band = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Band.class);
      return band;
    }
  }

  public void addVenue(Venue venue) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id)";
      con.createQuery(sql)
        .addParameter("band_id", this.getId())
        .addParameter("venue_id", venue.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Venue> getVenues() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT venue_id FROM bands_venues WHERE band_id = :band_id";
      List<Integer> venueIds = con.createQuery(sql)
        .addParameter("band_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Venue> venues = new ArrayList<Venue>();

      for (Integer venueId : venueIds) {
          String venueQuery = "SELECT * FROM venues WHERE id = :venueId";
          Venue venue = con.createQuery(venueQuery)
            .addParameter("venueId", venueId)
            .executeAndFetchFirst(Venue.class);
          venues.add(venue);
      }
      return venues;
    }
  }
}
