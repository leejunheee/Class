import java.awt.image.DataBufferDouble;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Attendance {
	private final String DATA;

	private static Scanner scan;
	private User user;
	private long eachWorkTime;
	private String workTime;
	private long weakWorkTime;
	private long positionWorkTime;
	private static Calendar c;
	private Calendar chulgeun; // 출근 시간
	private Calendar toegeun; // 퇴근 시간
	private Calendar overTime; // 연장 근무
	private String attendancePath;
	private String dayWorkingTimePath;

	public Attendance(User user) {
		DATA = "data/Contact.txt"; // Window
		// DATA = "data/Contact.txt"; // Mac

		eachWorkTime = 0;
		weakWorkTime = 0;
		positionWorkTime = 0;
		c = Calendar.getInstance();
		chulgeun = Calendar.getInstance();
		toegeun = Calendar.getInstance();
		overTime = Calendar.getInstance();
		this.user = user;
		attendancePath = "";
		scan = new Scanner(System.in);

	}

	private static String menu() {

		System.out.println("===================");
		System.out.println("1. 출근시간 입력");
		System.out.println("2. 퇴근시간 입력");
		System.out.println("3. 원하는 날짜의 근무시간 조회");
		System.out.println("4. 종료");
		System.out.println("===================");
		System.out.print("선택: ");

		String sel = scan.nextLine();

		return sel;

	}

	public void attendanceScreen() {

		System.out.println("[근태관리]");

		boolean loop = true;

		String sel = menu();

		if (sel.equals("1")) {
			setWorkingTime();
		} else if (sel.equals("2")) {

		} else if (sel.equals("3")) {
			searchWorkingTime();
		} else if (sel.equals("4")) {

		} else {
			loop = true;
		}

	}

	/**
	 * 출퇴근 시간 입력시 해당 경로 파일에 이름,출근시간,퇴근시간 생성
	 * 
	 * @author 2조
	 * @throws IOException
	 */
	public void setWorkingTime() {

		try {
			BufferedReader read = new BufferedReader(new FileReader(DATA));
			String line = "";

			while ((line = read.readLine()) != null) {
				String[] temp = line.split(",");
				if (temp[2].equals(this.user.getName())) {
					this.chulgeun.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 9, 00, 00);
					this.toegeun.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 18, 30, 00);

				}

			} // while

			/**
			 * 이름,출근시간,퇴근시간 으로 해당 파일에 저장.
			 */
			attendancePath = "data/attendance/working.txt";
			FileWriter fw = new FileWriter(attendancePath, true);
			String chulgeun = String.format("%tF %tT", this.chulgeun, this.chulgeun);
			String toegeun = String.format("%tF %tT", this.toegeun, this.toegeun);
			fw.write(this.user.getName());
			fw.write(",");
			fw.write(chulgeun);
			fw.write(",");
			fw.write(toegeun);
			fw.write("\r\n");
			fw.close();

			dayWorkingTime();
		} catch (Exception e) {
			System.out.println(e);
		}

	}// setWorkingTime

	/**
	 * 저장된 출퇴근 시간을 이용해 근무시간을 계산하여 해당 날짜의 근무시간 저장
	 * 
	 * @throws IOException
	 */
	public void dayWorkingTime() {

		try {
			BufferedReader read = new BufferedReader(new FileReader(attendancePath));

			String line = "";

			while ((line = read.readLine()) != null) {
				String temp[] = line.split(",");
				if (temp[0].equals(this.user.getName())) {
					eachWorkTime = (toegeun.getTimeInMillis() - chulgeun.getTimeInMillis()) / 1000 / 60;
					if (eachWorkTime > 60) {
						this.workTime = String.format("%d시간 %d분\n", eachWorkTime / 60, eachWorkTime % 60);
					}
				}

			} // while
//    	System.out.printf("%tF 근무시간: ",this.chulgeun); 
//    	System.out.println(this.workTime); 

			dayWorkingTimePath = "data/attendance/dayWorkingTimePath.txt";
			FileWriter fw = new FileWriter(dayWorkingTimePath, true);
			fw.write(this.user.getName());
			fw.write(",");
			fw.write(String.format("%tF, 근무시간 ", this.chulgeun));
			fw.write(",");
			fw.write(this.workTime);
			// fw.write("\r\n");
			fw.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}// DayWorkingTime

	public void searchWorkingTime() {

		try {
			BufferedReader read = new BufferedReader(new FileReader(dayWorkingTimePath));
			System.out.print("근무 시간을 확인하고 싶은 날짜를 입력하시오(YYYY-MM-DD): ");
			String date = scan.nextLine();

			String line = "";
			// String date = "2021-05-02";
			String str = "";

			while ((line = read.readLine()) != null) {
				String temp[] = line.split(",");
				if (temp[1].equals(date)) {
					str = temp[3];
				}
			} // while
			System.out.printf("%s님의 %s 근무시간은 %s입니다.\n ", this.user.getName(), date, str);
		} catch (Exception e) {
			System.out.println(e);
		}
	}// searchWorkingTime

}
