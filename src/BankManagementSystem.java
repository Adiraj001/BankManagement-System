import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Account implements Serializable {
    private int accountNumber;
    private String name;
    private int deposit;
    private char type;

    public void createAccount(int accountNumber, String name, char type, int deposit) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.type = Character.toUpperCase(type);
        this.deposit = deposit;
        System.out.println("\nAccount Created....");
    }

    public void showAccount() {
        System.out.println("\nAccount No : " + accountNumber);
        System.out.println("Account Holder Name : " + name);
        System.out.println("Type Of Account : " + type);
        System.out.println("Balance Amount : " + deposit);
    }

    public void modify(String name, char type, int deposit) {
        this.name = name;
        this.type = Character.toUpperCase(type);
        this.deposit = deposit;
    }

    public void dep(int amount) {
        deposit += amount;
    }

    public void draw(int amount) {
        deposit -= amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getDeposit() {
        return deposit;
    }

    public String getName() {
        return name;
    }

    public char getType() {
        return type;
    }

    public void report() {
        System.out.printf("%-10d %-15s %-5c %10d\n", accountNumber, name, type, deposit);
    }
}

public class BankManagementSystem extends JFrame {
    private JTextField txtAccountNumber, txtName, txtDeposit;
    private JComboBox<String> cmbType;
    private JTextArea textArea;

    public BankManagementSystem() {
        setTitle("Bank Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Account Number:"));
        txtAccountNumber = new JTextField();
        panel.add(txtAccountNumber);

        panel.add(new JLabel("Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Type (C/S):"));
        cmbType = new JComboBox<>(new String[]{"C", "S"});
        panel.add(cmbType);

        panel.add(new JLabel("Initial Deposit:"));
        txtDeposit = new JTextField();
        panel.add(txtDeposit);

        JButton btnCreate = new JButton("Create Account");
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });
        panel.add(btnCreate);

        JButton btnShow = new JButton("Show Account");
        btnShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAccount();
            }
        });
        panel.add(btnShow);

        add(panel, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void createAccount() {
        int accountNumber = Integer.parseInt(txtAccountNumber.getText());
        String name = txtName.getText();
        char type = cmbType.getSelectedItem().toString().charAt(0);
        int deposit = Integer.parseInt(txtDeposit.getText());

        Account ac = new Account();
        ac.createAccount(accountNumber, name, type, deposit);
        writeAccount(ac);
    }

    private void showAccount() {
        int accountNumber = Integer.parseInt(txtAccountNumber.getText());
        displaySp(accountNumber);
    }

    public static void writeAccount(Account ac) {
        try (ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("account.dat", true))) {
            outFile.writeObject(ac);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displaySp(int n) {
        boolean found = false;
        try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("account.dat"))) {
            while (true) {
                try {
                    Account ac = (Account) inFile.readObject();
                    if (ac.getAccountNumber() == n) {
                        textArea.setText("Account No : " + ac.getAccountNumber() + "\n" +
                                         "Account Holder Name : " + ac.getName() + "\n" +
                                         "Type Of Account : " + ac.getType() + "\n" +
                                         "Balance Amount : " + ac.getDeposit());
                        found = true;
                        break;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!found) {
            textArea.setText("Account Number Does Not Exist");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BankManagementSystem().setVisible(true);
            }
        });
    }
}
