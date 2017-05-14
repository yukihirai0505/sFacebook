package helpers

/**
  * Created by yukyh on 2017/05/14.
  */
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedCondition, WebDriverWait}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.selenium.WebBrowser

trait WebHelper extends WebBrowser {

  System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver")

  val captureDirectory = "src/test/resources"

  implicit val webDriver: WebDriver = new ChromeDriver()
  val delay = 10
  val webDriverWait = new WebDriverWait(webDriver, delay)

  setCaptureDir(captureDirectory)

  def snapshot = {
    capture to "MySnapshot.png"
  }

  def open(url: String) {
    go to url
  }

  def titlePage: String = {
    pageTitle
  }

  def setQueryString(strQuery: String) = {
    clickOn("q")
    textField("q").value = strQuery
    submit()
    Thread.sleep(5000)
  }

  def tearDown: Unit = {
    quit()
  }

  def waitSeconds(sec: Long) = {
    Thread.sleep(sec)
  }

  def findElementById(id: String): WebElement = {
    webDriver.findElement(By.id(id))
  }

  def findElementByName(name: String): WebElement = {
    webDriver.findElement(By.name(name))
  }

  def findElementByClassName(className: String): WebElement = {
    webDriver.findElement(By.className(className))
  }

  def waitId(id: String) = {
    waitBy(By.id(id))
  }

  def waitName(name: String) = {
    waitBy(By.name(name))
  }

  def waitUrlContains(key: String) = {
    webDriverWait.until(new ExpectedCondition[Boolean] {
      override def apply(d: WebDriver): Boolean =
        d.getCurrentUrl.contains(key)
    })
  }

  private def waitBy(by: By) = {
    webDriverWait.until(new ExpectedCondition[Boolean] {
      override def apply(d: WebDriver): Boolean =
        d.findElement(by).isDisplayed
    })
  }

}