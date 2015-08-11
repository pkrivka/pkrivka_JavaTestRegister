package register;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Formatter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * User interface of the application.
 */
public class ConsoleUI implements Serializable {
	/** register.Register of persons. */
	private Register register;

	private RegisterLoader reg = new RegisterLoader();
	/**
	 * In JDK 6 use Console class instead.
	 * 
	 * @see readLine()
	 */
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Menu options.
	 */
	private enum Option {
		PRINT, ADD, UPDATE, REMOVE, FIND, EXIT
	};

	public ConsoleUI(Register register) {

		this.register = register;
	}

	public ConsoleUI() throws FileNotFoundException, ClassNotFoundException, IOException {

		this.register = reg.registerLoad();
	}

	public void run() throws IOException, ClassNotFoundException {
		reg.registerFill(register);

		while (true) {
			switch (showMenu()) {
			case PRINT:
				printRegister();
				break;
			case ADD:
				addToRegister();
				break;
			case UPDATE:
				updateRegister();
				break;
			case REMOVE:
				removeFromRegister();
				break;
			case FIND:
				findInRegister();
				break;
			case EXIT:
				reg.writeRegister(register);
				return;
			}
		}
	}

	private String readLine() {
		// In JDK 6.0 and above Console class can be used
		// return System.console().readLine();

		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	private Option showMenu() {
		System.out.println("Menu.");
		for (Option option : Option.values()) {
			System.out.printf("%d. %s%n", option.ordinal() + 1, option);
		}
		System.out.println("-----------------------------------------------");

		int selection = -1;
		do {
			System.out.println("Option: ");
			selection = Integer.parseInt(readLine());
		} while (selection <= 0 || selection > Option.values().length);

		return Option.values()[selection - 1];
	}

	/**
	 * - pomocou StringBuildera vypise vsetky zaznamy z registra do konzoly
	 * 
	 * @param sb
	 *            novu objekt stringbuilder
	 * @param f
	 *            novy objekt formatter
	 */
	private void printRegister() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		for (int i = 0; i < register.getCount(); i++) {
			f.format("%3s", (i + 1) + ". ");
			f.format("%10s", register.getPerson(i) + "\n");
		}
		System.out.println(sb);
		f.close();
	}

	/**
	 * - precita udaje zo vstupu a vlozi ich do registra ako novu osobu pomocou
	 * premennych
	 * 
	 * @param name
	 *            zadane meno zo vstupu
	 * @param phoneNumber
	 *            zadane telefonne cislo zo vstupu
	 */
	private void addToRegister() {
		System.out.println("Enter Name: ");
		String name = readLine();
		System.out.println("Enter Phone Number: ");
		String phoneNumber = readLine();
		if (register.findPersonByName(name) != null
				&& register.findPersonByName(name).getPhoneNumber().equals(phoneNumber)) {
			System.out.println("Zadany pouzivatel uz existuje!!!");
		} else {
			register.addPerson(new Person(name, phoneNumber));
		}

	}

	/**
	 * - aktualizuje hodnoty v registry
	 * 
	 * @param person
	 *            vybrana osoba z registra
	 * @param meno
	 *            zadane meno zo vstupu
	 * @param cislo
	 *            zadane telefonne cislo zo vstupu
	 */
	private void updateRegister() {
		System.out.println("Enter index: ");
		int index = Integer.parseInt(readLine());
		Person person = register.getPerson(index - 1);
		System.out.format("%3s", index + ". " + person + "\n");
		System.out.println("Enter name: ");
		String meno = readLine();
		System.out.println("Enter phone number: ");
		String cislo = readLine();
		person.setName(meno);
		person.setPhoneNumber(cislo);
	}

	/**
	 * - prehladava register podla mena alebo podla cisla
	 * 
	 * @param otazka
	 *            vyber typu vyhladavania
	 * @param meno
	 *            nacitane meno zo vstupu
	 * @param cislo
	 *            nacitane cislo zo vstupu
	 * @param zaznam
	 *            najdeny zaznam v registry podla zadaneho mena alebo cisla
	 */
	private void findInRegister() {
		System.out.println("1 - search by name\n2 - search by number");
		String otazka = readLine();
		System.out.println(otazka);
		if ("1".equals(otazka)) {
			System.out.println("Enter name: ");
			String meno = readLine();
			Person zaznam = register.findPersonByName(meno);
			if (zaznam == null) {
				System.out.println("Dane meno nie je v databaze!");
			} else {
				System.out.println(zaznam);
			}
		} else if ("2".equals(otazka)) {
			System.out.println("Enter phone number: ");
			String cislo = readLine();
			Person zaznam = register.findPersonByPhoneNumber(cislo);
			if (zaznam == null) {
				System.out.println("Dane cislo nie je v databaze!");
			} else {
				System.out.println(zaznam);
			}
		} else {
			System.out.println("Musis si zvolit medzi 1 a 2 moznostou!");
		}
	}

	/**
	 * - vymaze osobu z registra na zaklade zadaneho indexu
	 * 
	 * @param index
	 *            vybrany index osoby, ktoru chceme zmazat
	 * @param person
	 *            vybrana osoba v registri podla zadaneho indexu
	 */
	private void removeFromRegister() {
		System.out.println("Enter index: ");
		int index = Integer.parseInt(readLine());
		Person person = register.getPerson(index - 1);
		register.removePerson(person);
	}

}
