package wastedPotentials;

public class projectCreator {
public static void main(String[] args) {
	
try {
	XLSReader xlsReader= new XLSReader("C:\\Users\\admin\\Desktop\\wastedPotential-master\\wastedPotentials\\src\\main\\resources\\Input\\inputForAutoSeleniumProjectGenerator.xls");
}
catch(Exception e) {
	System.out.println(e.getMessage());
}

}}

