package hello;

//package hu.csani.metrodom_timelapse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumClass {
	protected WebDriver driver;
	public static int wrong_captcha_count;

	public void setUp(Browser browser) {
		try {
			if (browser == Browser.chrome) {
				System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//				ChromeOptions options = new ChromeOptions();
				this.driver = new ChromeDriver();
			} else if (browser == Browser.phantomJS) {
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setJavascriptEnabled(true);
				caps.setCapability("takesScreenshot", true);
//				caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "phantomjs.exe");
				driver = new PhantomJSDriver(caps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setUpBigDimension() {
		this.driver.manage().window().setSize(new Dimension(2000, 5000));
	}

	protected void savePicutre(String username) throws Exception {
		WebElement picture = this.driver
				.findElement(By.xpath("//div[@class=\"_22yr2 _e0mru\"]/div[@class=\"_jjzlb\"]/img[@src]"));
		String url_string = picture.getAttribute("src");
		URL url = new URL(url_string);
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		String mainPath = String.valueOf(System.getProperty("user.dir")) + "\\pictures\\";
		FileOutputStream fos = new FileOutputStream(String.valueOf(mainPath) + username + "2.jpg");
		fos.write(response);
		fos.close();
	}

	protected String getSafeText(String xpath) {
		try {
			if (this.driver.findElements(By.xpath(xpath)).size() != 0) {
				return this.driver.findElement(By.xpath(xpath)).getText();
			}
			return "";
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return "";
		}
	}

	protected String getSafeAttributeText(String xpath, String attribute) {
		try {
			return this.driver.findElement(By.xpath(xpath)).getAttribute(attribute);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return "";
		}
	}

	protected BufferedImage takeScreenShootFull(String filename) throws IOException {
		String mainPath = String.valueOf(System.getProperty("user.dir")) + "\\pictures\\";
		File scrFile = ((TakesScreenshot) ((Object) this.driver)).getScreenshotAs(OutputType.FILE);
		BufferedImage in = ImageIO.read(scrFile);
		return in;
	}

	protected void goSleep(int seconds) {
		int j = 0;
		while (j < seconds) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			++j;
		}
	}

	protected void hitEnter() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(10);
		robot.keyRelease(10);
		System.out.println("Entert nyomtam");
	}

	protected void hitback2() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(8);
		robot.keyRelease(8);
		robot.keyPress(8);
		robot.keyRelease(8);
		System.out.println("esc nyomtam");
	}

	protected void scrollDown(int scrolls) {
		int i = 0;
		while (i < scrolls) {
			JavascriptExecutor jse = (JavascriptExecutor) ((Object) this.driver);
			jse.executeScript("window.scrollBy(0,-320)", "");
			jse.executeScript("window.scrollBy(0,650)", "");
			try {
				Thread.sleep(this.randInt(200, 500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			++i;
		}
	}

	public void tearDown() {
		this.driver.quit();
	}

	protected void loadingExecuter() {
		Thread one = new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(SeleniumClass.this.randInt(10232, 15234));
					SeleniumClass.sendEscKey();
				} catch (InterruptedException v) {
					System.out.println(v);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		};
		one.start();
	}

	public static void sendEscKey() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(27);
		robot.keyRelease(27);
		System.out.println("Escapet nyomtam");
	}

	public int randInt(int min, int max) {
		try {
			Random rand = new Random();
			int randomNum = rand.nextInt(max - min + 1) + min;
			return randomNum;
		} catch (Exception e) {
			return 0;
		}
	}

	public void goPreviousPage() {
		JavascriptExecutor js = (JavascriptExecutor) ((Object) this.driver);
		js.executeScript("window.history.go(-1)", new Object[0]);
	}

	public void teszt() {
		try {
			String youtube = "https://www.youtube.com/user/ClaireFabb";
			if (youtube != null && youtube.length() > 0) {
				this.driver.get(youtube);
				String subScribers = "";
				List<WebElement> findElements = this.driver.findElements(By.xpath(
						"//span[@class=\"yt-subscription-button-subscriber-count-branded-horizontal subscribed yt-uix-tooltip\"]"));
				subScribers = findElements.get(0).getText().replaceAll("[^0-9]", "");
				System.out.println(subScribers);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(this.driver.getCurrentUrl());
		}
	}

	public void getThePic() {
		this.opendirectly();
		System.out.println("Wait 2 sec");
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		System.out.println("Take a shoot");
		String filename = LocalDateTime.now().format(formatter);
		System.out.println(filename);
		File outputfile = null;
		this.takeScreenShoot(filename, outputfile);
	}

	private void takeScreenShoot(String filename, File outputfile) {
		try {
			BufferedImage takeScreenShootFull = this.takeScreenShootFull(filename);
			System.out.println("Done");
			System.out.println("Crop image");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int week = cal.get(3);
			String dateDir = String.valueOf(new SimpleDateFormat("MMMM").format(cal.getTime())) + "_" + week;
			String folder = "c:\\metrodom timelaps_new\\" + dateDir + "\\";
			new File(folder).mkdirs();
			String mainPath = String.valueOf(folder) + filename + ".png";
			outputfile = new File(mainPath);
			ImageIO.write((RenderedImage) takeScreenShootFull, "png", outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (outputfile.length() < 210000L) {
			System.out.println("Sajnos szar volt a k\u00e9p szal t\u00f6rl\u00e9s");
			try {
				Thread.sleep(20000L);
				Files.delete(outputfile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.takeScreenShoot(filename, outputfile);
		}
	}

	private BufferedImage cropImage(BufferedImage src, int x, int y, int w, int h) {
		BufferedImage dest = src.getSubimage(x, y, w, h);
		return dest;
	}

	void openAndScrollToVideo() {
		System.out.println("Open page");
		System.out.println("OPened, wait 10 sec");
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement bhouseFull = this.driver
				.findElement(By.xpath("(//div[@class='fp-header']/a[@class='fp-fullscreen fp-icon'])[2]"));
		System.out.println("Wait 5 sec");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		System.out.println("Scroll down");
		((JavascriptExecutor) ((Object) this.driver)).executeScript("arguments[0].scrollIntoView(true);", bhouseFull);
	}

	void opendirectly() {
		System.out.println("Open page");
		String directURL = "https://flowplayer.com/e/?t=Kamera1&c=eyJkZWJ1ZyI6ZmFsc2UsImRpc2FibGVkIjpmYWxzZSwiZnVsbHNjcmVlbiI6dHJ1ZSwia2V5Ym9hcmQiOnRydWUsInJhdGlvIjowLjU2MjUsImFkYXB0aXZlUmF0aW8iOmZhbHNlLCJydG1wIjowLCJwcm94eSI6ImJlc3QiLCJobHNRdWFsaXRpZXMiOnRydWUsInNwbGFzaCI6ZmFsc2UsImxpdmUiOnRydWUsImxpdmVQb3NpdGlvbk9mZnNldCI6MTIwLCJzd2YiOiIvL3JlbGVhc2VzLmZsb3dwbGF5ZXIub3JnLzcuMi4xL2Zsb3dwbGF5ZXIuc3dmIiwic3dmSGxzIjoiLy9yZWxlYXNlcy5mbG93cGxheWVyLm9yZy83LjIuMS9mbG93cGxheWVyaGxzLnN3ZiIsInNwZWVkcyI6WzAuMjUsMC41LDEsMS41LDJdLCJ0b29sdGlwIjp0cnVlLCJtb3VzZW91dFRpbWVvdXQiOjUwMDAsInZvbHVtZSI6IjEiLCJlcnJvcnMiOlsiIiwiVmlkZW8gbG9hZGluZyBhYm9ydGVkIiwiTmV0d29yayBlcnJvciIsIlZpZGVvIG5vdCBwcm9wZXJseSBlbmNvZGVkIiwiVmlkZW8gZmlsZSBub3QgZm91bmQiLCJVbnN1cHBvcnRlZCB2aWRlbyIsIlNraW4gbm90IGZvdW5kIiwiU1dGIGZpbGUgbm90IGZvdW5kIiwiU3VidGl0bGVzIG5vdCBmb3VuZCIsIkludmFsaWQgUlRNUCBVUkwiLCJVbnN1cHBvcnRlZCB2aWRlbyBmb3JtYXQuIFRyeSBpbnN0YWxsaW5nIEFkb2JlIEZsYXNoLiJdLCJlcnJvclVybHMiOlsiIiwiIiwiIiwiIiwiIiwiIiwiIiwiIiwiIiwiIiwiaHR0cDovL2dldC5hZG9iZS5jb20vZmxhc2hwbGF5ZXIvIl0sInBsYXlsaXN0IjpbXSwiaGxzRml4IjpmYWxzZSwiZGlzYWJsZUlubGluZSI6ZmFsc2UsImF1dG9wbGF5IjoiYXV0b3BsYXkiLCJjbGlwIjp7InRpdGxlIjoiS2FtZXJhMSIsInN1YnRpdGxlcyI6W10sInNvdXJjZXMiOlt7InNyYyI6Imh0dHA6Ly9udnIxLm1vYmlsZWd1YXJkLmh1L21ldHJvZG9tLXBhcmstMi0wYjNmZjExNWI2L2luZGV4Lm0zdTgiLCJzdWZmaXgiOiJtM3U4IiwidHlwZSI6ImFwcGxpY2F0aW9uL3gtbXBlZ3VybCJ9XX0sImR2ciI6ZmFsc2UsInBvc3RlciI6dHJ1ZSwiaG9zdG5hbWUiOiJtZXRyb2RvbS5odSIsIm9yaWdpbiI6Imh0dHA6Ly9tZXRyb2RvbS5odS9tZXRyb2RvbS1wYXJrL3ZpZGVvLW1wYyIsImpzIjpbXSwiY3NzIjpbXX0%3D&r=http%3A%2F%2Fmetrodom.hu%2Fmetrodom-park%2Fvideo-mpc";
		this.driver.get("https://metrodom.hu/metrodom-park/video-mpc");
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JavascriptExecutor jse = (JavascriptExecutor) ((Object) this.driver);
		jse.executeScript("window.scrollBy(0,270)", "");
		System.out.println("wait end");
		this.driver.findElement(By.xpath("(//a[@class='fp-fullscreen fp-icon'])[2]")).click();
		System.out.println("loaded");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void refreshPage() {
		this.driver.get(this.driver.getCurrentUrl());
		System.out.println("Refresh done!");
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String test() {
		System.out.println("Open page");
		this.driver.get("https://ts4.travian.hu/dorf1.php");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.findElement(By.xpath("//input[@name='name']")).sendKeys("SüniÚr");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("Battlefield2");

		driver.findElement(By.xpath("//button[@type='submit']")).click();

		System.out.println(driver.getTitle());

		StringBuilder sb = new StringBuilder();

		List<WebElement> findElements = driver.findElements(By.xpath("//area[@href]"));
		for (WebElement webElement : findElements) {
			System.out.println(webElement.getAttribute("alt"));
			sb.append(webElement.getAttribute("alt")).append("\n");
		}

		return sb.toString();

	}

}
