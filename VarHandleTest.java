package varhandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;

class Employee {
	public String name;
	public int id;
	public static int companyId = 15;
	public int[] empPayment = new int[] {4, 7, 3 , 8, 9, 2, 5};
	private long mobileNo = 94237481596L;
	
	public Employee(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	static public String getKlass() {
		return "Employee.class";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getLastName(String name) {
		String[] splitString = name.split(" ", -1);
		System.out.println(splitString[2]);
		return splitString[splitString.length - 1];
	}
	
	public void display(String fname, String lname) {
		System.out.println("NAME : " + fname + " " + lname);
	}
	
	public void display(String[] name) {
		System.out.println("name: " + Arrays.deepToString(name));
	}
	
}

public class VarHandleTest {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
		VarHandle Name = MethodHandles.lookup().findVarHandle(Employee.class, "name", String.class);
		Employee employee = new Employee("Parul", 48);
		Name.set(employee, "Parul Raut");
		System.out.println("Name of employee " + Name.get(employee));
		
		Thread thread1 = new Thread() {
			@Override
			public void run() {
				System.out.println("Name from thread1 " + Name.get(employee));
				Name.setVolatile(employee, "Parul_Raut");
			}
		};

		Thread thread2 = new Thread() {
			@Override
			public void run() {
				System.out.println("Name from thread2 " + Name.get(employee));
				Name.setVolatile(employee, "Parul Suresh Raut");
			}
		};
		thread1.start();
		thread2.start();
		
		thread1.join();
		thread2.join();
		
		System.out.println("modified name of employee " + Name.getVolatile(employee));
		
	    VarHandle CompanyId = MethodHandles.lookup().findStaticVarHandle(Employee.class, "companyId", int.class);
		System.out.println("Company Id of Employee " + (int)CompanyId.get());
		
		VarHandle EmpPayment = MethodHandles.arrayElementVarHandle(int[].class);
		System.out.println("Payment of employee in the month of April " + EmpPayment.get(employee.empPayment, 3));
		
	}

}