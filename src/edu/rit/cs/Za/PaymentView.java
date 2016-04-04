package edu.rit.cs.Za;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PaymentView {
	private JFrame frame;
	private JPanel paymentPanel; 
	private JPanel creditPanel;
	public void runGUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void initialize(){
		frame = new JFrame();
		paymentPanel = new JPanel(new BorderLayout());
		creditPanel = new JPanel(new GridBagLayout());
		creditPanel.setBorder(new EmptyBorder(50,0,50,0));
		JPanel radioPanel = new JPanel(new GridLayout(1,2));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(new Dimension(600,300));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(paymentPanel, BorderLayout.NORTH);
		JRadioButton CRButton = new JRadioButton("Pay by credit card");
		CRButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				paymentPanel.add(creditPanel, BorderLayout.CENTER);
				frame.validate();
			}
		});
		radioPanel.add(CRButton);
		JRadioButton CashButton = new JRadioButton("Pay by cash");
		CashButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				paymentPanel.remove(creditPanel);
				frame.validate();
			}
			
		});
		radioPanel.add(CashButton);
		paymentPanel.add(radioPanel, BorderLayout.NORTH);
		ButtonGroup RButton = new ButtonGroup();
		RButton.add(CRButton);
		RButton.add(CashButton);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel cnumLabel = new JLabel("Credit card number:"); 
		creditPanel.add(cnumLabel, gbc);
		gbc.gridx++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		JComboBox cardNumBox = new JComboBox();
		cardNumBox.setPreferredSize(new Dimension(250,20));
		cardNumBox.setEditable(true);
		JTextField expireDateField = new JTextField();
		expireDateField.setPreferredSize(new Dimension(50,20));
		JTextField securityField = new JTextField();
		securityField.setPreferredSize(new Dimension(50,20));
		creditPanel.add(cardNumBox, gbc);
		gbc.gridy++;
		gbc.gridx--;
		JLabel expLabel = new JLabel("Expire Date:");
		creditPanel.add(expLabel, gbc);
		gbc.gridx++;
		creditPanel.add(expireDateField, gbc);
		gbc.gridy++;
		gbc.gridx--;
		JLabel secLabel = new JLabel("Security Code:");
		creditPanel.add(secLabel, gbc);
		gbc.gridx++;
		creditPanel.add(securityField, gbc);
		paymentPanel.add(creditPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton selectButton = new JButton("Submit");
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