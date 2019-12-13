package chrome;

import org.testng.annotations.Test;

import utils.Constant;
import utils.ExcelUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;

public class TestSignUp {
	
    String driverPath = Constant.Path_Drive_Gecko;
    public ChromeDriver driver ; 
    
    //Element SignUp Form
    WebElement btnSignUp;
    WebElement inputUsername;
    WebElement inputEmail;
    WebElement btnSubmit;
    
    //Element EditAccount Form
    WebElement inputRealName;
    WebElement inputPassword;
    WebElement inputConfirmPassword;
    WebElement btnUpdateUser;
    
    
    String strUserName = "Vy02";
    String strEmailTest = "kcpmhuong02@gmail.com";
    String strPasswordEmail = "Kiemthutudong02";
    
    
 
    String strEmailSender = "kcpmhuong01@gmail.com";
    String strFilePhP = "verify.php";
    
    String strRealName = "Nguyen Thanh Huong";
    String strPassUser = "123123";
    String strConfirmPassUser = "123123";
   
    
    String strUserLogin = "Vy02";
    String strPasswordLogin = "123123";
    
    
  public void launchdriver(String url) {
	  System.out.println("launching Chrome driver");
      
      System.setProperty("webdriver.chrome.driver", Constant.Path_Drive_Chrome);
      
      //Initialize driver
      driver = new ChromeDriver();
      driver.manage().window().maximize();
      driver.get(url);
     
  }
  
  @Test
  public void SignUpLogInTest() throws Exception {
	  // Asscess Data
	  ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData , "Sheet1");
	  XSSFSheet sheet = ExcelUtils.getSheetName("Sheet1");
	  int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	  
	//Create a loop over all the rows of excel file to read it
	    for (int i = 1; i < rowCount; i++) {
	        Row row = sheet.getRow(i);
	        //Create a loop to print cell values in a row
	        for (int j = 0; j < row.getLastCellNum(); j++) {
	        	
	        	String str = ExcelUtils.getCellData(i,  j);
	        	System.out.println(str);
	        	
	        	switch(j){
	        		case 0:
	        			strUserName = str;
	        			break;
	        		case 1:
	        			strEmailTest = str;
	        			break;
	        		case 2:
	        			strPasswordEmail = str;
	        			break;
	        		case 3:
	        			strRealName = str;
	        			break;
	        		case 4:
	        			strConfirmPassUser = strPassUser = str;
	        			break;
	        		default: break;
	        			
	        	}

	        }
	        boolean checkSignUpSuccess = runSignUpFlow(strUserName,strEmailTest, strPasswordEmail,strRealName,strPassUser,strConfirmPassUser );
	        
	        if(checkSignUpSuccess) {
	        	
	        	runLoginFlow(strUserName, strPassUser);
	        	
	        }
	    } 
  }
  
  private void runLoginFlow(String strUserName2, String strPassUser2) {
	  
	// TODO Auto-generated method stub
	  launchdriver(Constant.URL_LogIn);
	  
	  driver.findElementByXPath("//input[@id='username']").sendKeys(strUserName2);;
	  driver.findElementByXPath("//input[@type='submit']").click();
	  WebDriverWait wait = new WebDriverWait(driver, 100);
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']")));
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']"))); 
	  driver.findElementByXPath("//input[@type='password']").sendKeys(strPassUser2);
	  driver.findElementByXPath("//input[@type='submit']").click();
	  
	  driver.quit();
	  
}

public boolean runSignUpFlow(String strUserName, String strEmailTest, String strPasswordEmail, String strRealName, String strPassUser, String strConfirmPassUser) throws InterruptedException {
	  
	     launchdriver(Constant.URL_SignUp);
	     
	   //invoke Elements on pgae: SignUp
	      inputUsername = driver.findElement(By.cssSelector("input[class='form-control autofocus']"));
	      inputEmail = driver.findElement(By.cssSelector("input[class='form-control']"));
	      btnSubmit = driver.findElement(By.cssSelector("input[class='width-40 pull-right btn btn-success btn-inverse bigger-110']"));
	  //SenKey Elements
		 inputUsername.sendKeys(strUserName);
		 inputEmail.sendKeys(strEmailTest);
		 btnSubmit.click(); 
		 
		 Thread.sleep(1000);
		 //Login_Gmail
		 driver.get(Constant.URL_LoginMail);
		 
		 driver.findElementByXPath("//input[@id='identifierId']").sendKeys(strEmailTest);
		 driver.findElementByXPath("//div[@id='identifierNext']").click();
		 
		 WebDriverWait wait = new WebDriverWait(driver, 100);
		 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']")));
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
		 driver.findElementByXPath("//input[@type='password']").sendKeys(strPasswordEmail);
		 driver.findElementByXPath("//div[@id='passwordNext']").click();
		 
		 
		 try {
	         TimeUnit.SECONDS.sleep(5);
	     }catch (InterruptedException ex){
	         ex.printStackTrace();
	     }
		//find mantis's mail
	     WebElement mantisMailTitle = driver.findElements(By.xpath("//*[@email='kcpmhuong01@gmail.com'][1]//ancestor::tr")).get(0);
	     if (mantisMailTitle!=null)
	         mantisMailTitle.click();

	     try {
	         TimeUnit.SECONDS.sleep(1);
	     }catch (InterruptedException ex){
	         ex.printStackTrace();
	     }

	     Thread.sleep(2000);
		//find the verified link inside mail
	     List verifiedLinks = driver.findElements(By.xpath("//a[contains(@href, 'http://localhost:8888/mantisbt-2.8.0/verify.php')]"));

	     WebElement link = verifiedLinks.size() == 0 ? null : (WebElement)verifiedLinks.get(verifiedLinks.size()-1);
	     if (link != null)
	     {
	         String href = link.getAttribute("href");
	         driver.get(href);

	         try {
	             Alert alert = driver.switchTo().alert();
	             Thread.sleep(1000);
	             alert.accept();
	         }catch (Exception ex)
	         {

	         }
	     }
	     // Edit Account
	     inputRealName = driver.findElementByXPath("//input[@id='realname']");
	     inputRealName.sendKeys(strRealName);
	     
	     inputPassword = driver.findElementByXPath("//input[@id='password']");
	     inputPassword.sendKeys(strPassUser);
	     
	     inputConfirmPassword = driver.findElementByXPath("//input[@id='password-confirm']");
	     inputConfirmPassword.sendKeys(strConfirmPassUser);
	     
	     btnUpdateUser = driver.findElementByXPath("//button[@type='submit']");
	     btnUpdateUser.click();
	     
	     //Load Page Login
	     
	     String strUrlCheckupdate = "http://localhost:8888/mantisbt-2.8.0/account_update.php";
	     
	     String strUrlUpdate =  driver.getCurrentUrl();
	     if(strUrlUpdate.equals(strUrlCheckupdate)) {
	    	 System.out.println("Update Success!");
	    	 driver.quit();
	    	 return true;
	    	//Finish Brower
	    	
	     }
	     return false;
	     
	  
  }

}
