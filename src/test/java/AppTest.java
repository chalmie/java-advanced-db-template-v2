import org.junit.*;
import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import org.apache.commons.lang.WordUtils;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
      goTo("http://localhost:4567/");
      assertThat(pageSource()).contains("");
  }

  @Test
  public void addsBandAndDisplaysOnSamePage_true() {
      goTo("http://localhost:4567/");
      Band newBand = new Band("brand new");
      newBand.firstToUppercase();
      newBand.save();
      goTo("http://localhost:4567/");
      assertThat(pageSource()).contains("Brand New");
  }

  @Test
  public void addsVenueAndDisplaysOnSamePage_true() {
      goTo("http://localhost:4567/");
      Venue newVenue = new Venue("modacenter");
      newVenue.firstToUppercase();
      newVenue.save();
      goTo("http://localhost:4567/");
      assertThat(pageSource()).contains("Modacenter");
  }
}
