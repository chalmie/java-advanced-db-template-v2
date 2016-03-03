import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class VenueTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Venue.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Venue firstVenue = new Venue("Modacenter");
    Venue secondVenue = new Venue("Modacenter");
    assertTrue(firstVenue.equals(secondVenue));
  }

  @Test
  public void save_savesObjectIntoDatabase() {
    Venue myVenue = new Venue("Modacenter");
    myVenue.save();
    Venue savedVenue = Venue.all().get(0);
    assertTrue(savedVenue.equals(myVenue));
  }

  @Test
  public void save_assignsIdToObject() {
    Venue myVenue = new Venue("Modacenter");
    myVenue.save();
    Venue savedVenue = Venue.all().get(0);
    assertEquals(myVenue.getId(), savedVenue.getId());
  }

  @Test
  public void find_findsVenueInDatabase_true() {
    Venue myVenue = new Venue("Modacenter");
    myVenue.save();
    Venue savedVenue = Venue.find(myVenue.getId());
    assertTrue(myVenue.equals(savedVenue));
  }

  @Test
  public void addBand_addsBandToVenue() {
    Band myBand = new Band("Brand New");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myVenue.addBand(myBand);
    Band savedBand = myVenue.getBands().get(0);
    assertTrue(myBand.equals(savedBand));
  }

  @Test
  public void getBands_returnsAllBands_ArrayList() {
    Band myBand = new Band("Brand New");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myVenue.addBand(myBand);
    List savedBands = myVenue.getBands();
    assertEquals(savedBands.size(), 1);
  }

  @Test
  public void delete_deletesAllVenuesAndListsAssoicationes() {
    Band myBand = new Band("Brand New");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myVenue.addBand(myBand);
    myVenue.delete();
    assertEquals(myBand.getVenues().size(), 0);
  }
}
