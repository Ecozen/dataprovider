package mytestng.dataprovider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.test.utils.DataReader;
import io.test.utils.RecordHandler;
import io.test.utils.SheetUtils;



public class dataproviderTest {
	
    private DataReader myInputData;
    public String getTestName() {
        return "API Test";
    }
    
//   private String userDir = System.getProperty("user.dir");
   String filePath = "";
//   String templatePath =  userDir + File.separator + "http_request_template.txt";
    
    XSSFWorkbook wb = null;
    XSSFSheet inputSheet = null;
    XSSFSheet baselineSheet = null;
    XSSFSheet outputSheet = null;
    XSSFSheet comparsionSheet = null;
    XSSFSheet resultSheet = null;
    
    @BeforeTest
    @Parameters("workBook")
    public void setup(String path) {
        filePath = path;
     
        try {
            wb = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputSheet = wb.getSheet("Input");
        baselineSheet = wb.getSheet("Baseline");

        SheetUtils.removeSheetByName(wb, "Output");
        SheetUtils.removeSheetByName(wb, "Comparison");
        SheetUtils.removeSheetByName(wb, "Result");
        outputSheet = wb.createSheet("Output");
        comparsionSheet = wb.createSheet("Comparison");
        resultSheet = wb.createSheet("Result");

    }
	
	
    @DataProvider(name = "WorkBookData")
    protected Iterator<Object[]> testProvider(ITestContext context) {

        List<Object[]> test_IDs = new ArrayList<Object[]>();

            myInputData = new DataReader(inputSheet, true, true, 0);

            // sort map in order so that test cases ran in a fixed order
            Map<String, RecordHandler> sortmap = new TreeMap<String,RecordHandler>(new Comparator<String>(){

				public int compare(String key1, String key2) {
					return key1.compareTo(key2);
				}
            	
            });
            
            sortmap.putAll(myInputData.get_map());
           
            
            for (Map.Entry<String, RecordHandler> entry : sortmap.entrySet()) {
                String test_ID = entry.getKey();
                String test_case = entry.getValue().get("TestCase");
                if (!test_ID.equals("") && !test_case.equals("")) {
                    test_IDs.add(new Object[] { test_ID, test_case });
                }
            }
            
//            new DataReader(baselineSheet, true, true, 0);

        return test_IDs.iterator();
    }
  @Test(dataProvider="WorkBookData")
  public void f(String test_ID,String test_case) {
	  System.out.println(test_ID+":"+myInputData.get_record(test_ID).get("name"));
  }
}
