package chrome;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.Constant;
import utils.ExcelUtils;

public class TestSetRoles {
	
	public ChromeDriver driver ; 
	
	 WebElement manageUserTab;
	 WebElement user ;
	 WebElement dropbox ;
	
	
@BeforeTest
public void launchdriver() throws InterruptedException{
		  System.out.println("launching Chrome driver");
	      System.setProperty("webdriver.chrome.driver", Constant.Path_Drive_Chrome);
	      
	      //Initialize driver
	      driver = new ChromeDriver();
	      driver.manage().window().maximize();
	      driver.get(Constant.URL_LogIn);
	      
	      //Login with ADMIN
	      driver.findElementByXPath("//input[@id='username']").sendKeys(Constant.userAdmin);;
		  driver.findElementByXPath("//input[@type='submit']").click();
		  WebDriverWait wait = new WebDriverWait(driver, 100);
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']")));
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']"))); 
		  driver.findElementByXPath("//input[@type='password']").sendKeys(Constant.passAdmin);
		  driver.findElementByXPath("//input[@type='submit']").click();

		  Thread.sleep(2000);
	      
	      
	 }
@Test
public void setRolesFlow() throws Exception {
	  // Asscess Data
	  ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData , "Sheet2");
	  XSSFSheet sheet = ExcelUtils.getSheetName("Sheet2");
	  int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	//Create a loop over all the rows of excel file to read it
	    for (int i = 1; i < rowCount; i++) {
	        	String strUserName = ExcelUtils.getCellData(i,  0);
	        	
	        	setRole(strUserName);
	        	
	        	
	        	Thread.sleep(2000);
	     }
	        
	} 
  
 public void setRole(String userName) throws InterruptedException {
	  
	  driver.findElement(By.xpath("//a[text() = 'Manage Users']")).click();
	  
	  driver.findElement(By.xpath(String.format("//a[text() = '%s']", userName))).click();
	  dropbox = driver.findElement(By.id("edit-access-level"));
	  dropbox.click();
	  
	  WebElement option;
	  if(Integer.parseInt(userName.substring(userName.length()-1)) % 2 == 0){
          option = driver.findElement(By.xpath("//option[text()='manager']"));
      }
      else
          option = driver.findElement(By.xpath("//option[text()='developer']"));
	  if(option != null)   option.click();
	
	  driver.findElement(By.xpath("//input[@value = 'Update User']")).click();
	  
	  Thread.sleep(1500);
  }
  
  @AfterTest
  public void quitBrower() {
	  driver.quit();
 }
}
