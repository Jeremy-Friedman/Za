package edu.rit.cs.Za.ui;

/**
 * SignupView.java
 * Contributor(s):  Yihao Cheng (yc7816@rit.edu)
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.rit.cs.Za.CreditCard;
import edu.rit.cs.Za.Month;
import edu.rit.cs.Za.OrderManager;
import edu.rit.cs.Za.OrderType;
import edu.rit.cs.Za.PaymentMethod;
import edu.rit.cs.Za.ProfileManager;

public class PaymentView {
	private JFrame frame;
	private JPanel paymentPanel; 
	private JPanel creditPanel;
	private JPanel crUserPanel;
	private JPanel userPanel;
	private GridBagConstraints gbc;
	
	private JComboBox<CreditCard> cardNumBox;
	private JRadioButton CRButton;
	private JRadioButton CashButton;
	private JTextField expireDateField;
	private JTextField userField;
	private JTextField securityField;
	private JCheckBox saveCB;
	private JButton removeCR;
	
	private boolean includeAddr;
	private long userID;
	private OrderType orderType;
	private List<CreditCard> creditCards;
	private Map<String,Integer> orderItems;
	/**
	 * PaymentView: Constructor
	 * @param includeAddr
	 */
	public PaymentView(long userID, boolean includeAddr, Map<String,Integer> orderItems, OrderType type){
		this.userID = userID;
		this.includeAddr = includeAddr;
		this.orderItems = orderItems;
		this.orderType = type;
		try {
			creditCards = ProfileManager.getCreditCards(this.userID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
		populateCrCard();
	}
	
	private void populateCrCard(){
		if(creditCards.size() != 0){
			for(CreditCard card : creditCards){
				cardNumBox.addItem(card);
			}
		}
	}
	
	/**
	 * runGUI: Show the UI
	 */
	public void runGUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * initialize: initializing the frame
	 */
	private void initialize(){
		frame = new JFrame();
		paymentPanel = new JPanel(new BorderLayout());
		creditPanel = new JPanel(new GridBagLayout());
		userPanel = new JPanel (new GridBagLayout());
		crUserPanel = new JPanel(new GridBagLayout());
		creditPanel.setBorder(new EmptyBorder(50,0,50,0));
		JPanel radioPanel = new JPanel(new GridLayout(1,2));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(new Dimension(600,300));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(paymentPanel, BorderLayout.NORTH);
		CRButton = new JRadioButton("Pay by credit card");
		CRButton.setSelected(true);
		CRButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!includeAddr){
					gbc.gridx = 0;
					gbc.gridy = 1;
					crUserPanel.add(creditPanel, gbc);
					frame.validate();
				}
			}
		});
		radioPanel.add(CRButton);
	    CashButton = new JRadioButton("Pay by cash");
		CashButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!includeAddr){
					crUserPanel.remove(creditPanel);
				}
				frame.validate();
			}
			
		});
		radioPanel.add(CashButton);
		paymentPanel.add(radioPanel, BorderLayout.NORTH);
		ButtonGroup RButton = new ButtonGroup();
		RButton.add(CRButton);
		RButton.add(CashButton);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel cnumLabel = new JLabel("Credit card number:"); 
		creditPanel.add(cnumLabel, gbc);
		gbc.gridx++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		cardNumBox = new JComboBox<CreditCard>();
		cardNumBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<CreditCard> cb = (JComboBox<CreditCard>) e.getSource();
				if(cb.getSelectedItem() instanceof CreditCard){
					CreditCard item =  (CreditCard) cb.getSelectedItem();
					removeCR.setEnabled(true);
					expireDateField.setText(Integer.toString(item.expirationMonth.value() + 1) + "/" + item.expirationYear);
					securityField.setText(item.securityCode);
				}else{
					removeCR.setEnabled(false);
				}
			}
		});
		cardNumBox.setPreferredSize(new Dimension(250,20));
		cardNumBox.setEditable(true);
		expireDateField = new JTextField();
		expireDateField.setPreferredSize(new Dimension(50,20));
		securityField = new JTextField();
		securityField.setPreferredSize(new Dimension(50,20));
		creditPanel.add(cardNumBox, gbc);
		removeCR = new JButton("Remove");
		removeCR.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(cardNumBox.getSelectedItem() instanceof CreditCard){
					CreditCard card = (CreditCard) cardNumBox.getSelectedItem();
					try {
						ProfileManager.removeCreditCard(userID, card.cardNumber);
						removeCR.setEnabled(false);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		removeCR.setEnabled(false);
		gbc.gridx++;
		creditPanel.add(removeCR);
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel expLabel = new JLabel("Expire Date(month/year):");
		creditPanel.add(expLabel, gbc);
		gbc.gridx++;
		creditPanel.add(expireDateField, gbc);
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel secLabel = new JLabel("Security Code:");
		creditPanel.add(secLabel, gbc);
		gbc.gridx++;
		creditPanel.add(securityField, gbc);
		saveCB = new JCheckBox("Save Payment info");
		gbc.gridy++;
		gbc.gridx = 0;
		creditPanel.add(saveCB, gbc);
		if(includeAddr){
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.weighty = 1;
			gbc.weightx = 1;
			JLabel userLabel = new JLabel("UserName:");
			userPanel.add(userLabel, gbc);
			userField = new JTextField();
			userField.setPreferredSize(new Dimension(150,20));
			gbc.gridx++;
			userPanel.add(userField, gbc);
		}
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		crUserPanel.add(userPanel, gbc);
		gbc.gridy++;
		if(!includeAddr){
			crUserPanel.add(creditPanel, gbc);
		}
		paymentPanel.add(crUserPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton selectButton = new JButton("Submit");
		selectButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, Object> orderDetail = new HashMap<String, Object>();
				if(!includeAddr){
					if(CRButton.isSelected()){
						if(cardNumBox.getSelectedItem() != null &&
								expireDateField.getText() != null && 
								expireDateField.getText().matches("^((0[1-9])|(1[0-2]))\\/(\\d{2})$") &&
								securityField.getText() != null &&
								orderItems.size() != 0){
							try {
								if((Integer.parseInt(expireDateField.getText().substring(0, 2))) > 12){
									return;
								}
								orderDetail.put("active", true);
								orderDetail.put("pay_method", PaymentMethod.parsePaymentMethod("CARD"));
								long orderID = OrderManager.createOrder(userID, orderType , orderItems);
								OrderManager.modifyOrder(orderID, orderDetail);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}else{
							return;
						}
					}else{
						if(orderItems.size() == 0){
							return;
						}
						orderDetail.put("active", true);
						orderDetail.put("pay_method", PaymentMethod.parsePaymentMethod("CASH"));
						long orderID;
						try {
							orderID = OrderManager.createOrder(userID, orderType , orderItems);
							OrderManager.modifyOrder(orderID, orderDetail);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if(saveCB.isSelected()){
						try {
							ProfileManager.addCreditCard(userID,cardNumBox.getSelectedItem().toString(), securityField.getText(), 
									Month.parseMonth(Integer.parseInt(expireDateField.getText().substring(0, 2)) - 1), 
									Integer.parseInt(expireDateField.getText().substring(3)));
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else{
					if(userField.getText().isEmpty()){
						return;
					}
					try {
						long custId = ProfileManager.getPersonID(userField.getText());
						if(custId >= 0){
							orderDetail.put("active", true);
							if(CRButton.isSelected()){
								orderDetail.put("pay_method", PaymentMethod.parsePaymentMethod("CARD"));
							}else{
								orderDetail.put("pay_method", PaymentMethod.parsePaymentMethod("CASH"));
							}
							long orderID = OrderManager.createOrder(custId, orderType , orderItems);
							OrderManager.modifyOrder(orderID, orderDetail);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "Order successfully placed!");
				frame.dispose();
			}
			
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
			
		});
		bottomPanel.add(selectButton);
		bottomPanel.add(cancelButton);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
}
