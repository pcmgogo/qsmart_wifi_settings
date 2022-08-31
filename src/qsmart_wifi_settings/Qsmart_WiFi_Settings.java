package qsmart_wifi_settings;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

public class Qsmart_WiFi_Settings {
    public static JFrame MainFrame;
    public static JPanel testPanel, ipPanel, wifiPanel;
    public static JSpinner ticketList,counterList;
    public static JTextField screenIpAddress, IpAddress, GwAddress, NmAddress, wifiSSID, wifiPassword;
    public static JCheckBox dhcpUsage;
    public static String version = "1.1.0";

    
    public static void main(String[] args) {
        try{
            MainFrame = new JFrame();
            MainFrame.setTitle("Q-smart WiFi Settings - "+version);
            MainFrame.setUndecorated(false);
            MainFrame.setResizable(false);
            MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int w = 330;
            int h = 490;
            MainFrame.getContentPane().setPreferredSize(new Dimension(w,h));
            MainFrame.pack();
            //MainFrame.setSize(w,h);
            //MainFrame.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width - w)/2 , (Toolkit.getDefaultToolkit().getScreenSize().height - h)/2 , w, h);

            testPanel = new JPanel(new SpringLayout());
            testPanel.setBorder(new TitledBorder("Test Settings"));
            testPanel.setSize(300, 120);
            testPanel.setBounds(10, 10, 300, 120);
            JLabel screenipLabel = new JLabel("IP Adress");
            testPanel.add(screenipLabel);
            screenIpAddress = new JTextField("10.10.10.1");
            testPanel.add(screenIpAddress);
            JLabel ticketLabel = new JLabel("Ticket Number");
            JLabel counterLabel = new JLabel("Counter Number");
            testPanel.add(ticketLabel);
            SpinnerModel ticket_model1 = new SpinnerNumberModel(1, 1, 9999, 1);
            ticketList = new JSpinner(ticket_model1);
            testPanel.add(ticketList);
            testPanel.add(counterLabel);
            SpinnerModel counter_model1 = new SpinnerNumberModel(1, 1, 99, 1);
            counterList = new JSpinner(counter_model1);
            testPanel.add(counterList);
            SpringUtilities.makeGrid(testPanel, 3, 2, 5, 5, 5, 5);
            JButton sendButton = new JButton("Send Test");
            sendButton.setSize(300, 30);
            sendButton.setBounds(10, 130, 300, 30);
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Random r = new Random();
                        int carrier = r.nextInt(9)+1;
                        int carrier_calc = r.nextInt(9)+1;
                        int counter = Integer.parseInt(counterList.getValue().toString());
                        int ticket = Integer.parseInt(ticketList.getValue().toString());
                        int carrier_mlt = counter * carrier;

                        if(carrier_calc % 2 == 0){
                            ticket = ticket - carrier_mlt;
                            counter = counter + carrier_mlt;
                        }
                        else{
                            ticket = ticket + carrier_mlt;
                            counter = counter - carrier_mlt;
                        }
                        if(ticket<0){
                            ticket = ticket + 65536;
                        }
                        if(ticket>65535){
                            ticket = ticket - 65535;
                        }
                        if(counter<0){
                            counter = counter + 256;
                        }
                        if(counter>255){
                            counter = counter - 255;
                        }
                        String message = carrier+"#"+carrier_calc+"#"+counter+"#"+ticket;
                        System.out.println("Sending data...");
                        SocketLibrary.SocketClient.send_answer_to_client(screenIpAddress.getText(), 2750, message, 10000, null);
                        System.out.println("Data sent");
                    }
                    catch(Exception ex){
                        
                    }
                }
            });
            
            ipPanel = new JPanel(new SpringLayout());
            ipPanel.setBorder(new TitledBorder("IP Settings"));
            ipPanel.setSize(300, 320);
            ipPanel.setBounds(10, 170, 300, 140);
            JLabel ipLabel = new JLabel("IP Adress");
            ipPanel.add(ipLabel);
            IpAddress = new JTextField("10.10.10.1");
            ipPanel.add(IpAddress);
            
            JLabel gwLabel = new JLabel("Gateway");
            ipPanel.add(gwLabel);
            GwAddress = new JTextField("10.10.10.1");
            ipPanel.add(GwAddress);

            JLabel nmLabel = new JLabel("Netmask");
            ipPanel.add(nmLabel);
            NmAddress = new JTextField("255.255.255.0");
            ipPanel.add(NmAddress);

            JLabel dhcpLabel = new JLabel("DHCP Usage");
            ipPanel.add(dhcpLabel);
            dhcpUsage = new JCheckBox("");
            ipPanel.add(dhcpUsage);
            
            SpringUtilities.makeGrid(ipPanel, 4, 2, 5, 5, 5, 5);
            JButton saveIpButton = new JButton("Save Ip Conf.");
            saveIpButton.setSize(300, 30);
            saveIpButton.setBounds(10, 310, 300, 30);
            saveIpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(dhcpUsage.isSelected()){
                        System.out.println("DHCP using");
                        String message = "ServiceDHCP:DHCP#"+IpAddress.getText().toString();
                        SocketLibrary.SocketClient.send_answer_to_client(screenIpAddress.getText(), 2750, message, 10000, null);
                    }
                    else{
                        System.out.println("Static IP using");
                        String message = "ServiceDHCP:Static#"+IpAddress.getText().toString()+"#"+GwAddress.getText().toString()+"#"+NmAddress.getText().toString();
                        System.out.println("Message:"+message);                        
                        SocketLibrary.SocketClient.send_answer_to_client(screenIpAddress.getText(), 2750, message, 10000, null);
                    }
                }
            });
            
            wifiPanel = new JPanel(new SpringLayout());
            wifiPanel.setBorder(new TitledBorder("WiFi Settings"));
            wifiPanel.setSize(300, 120);
            wifiPanel.setBounds(10, 350, 300, 90);
            JLabel ssidLabel = new JLabel("SSID");
            wifiPanel.add(ssidLabel);
            wifiSSID = new JTextField("Qsmart");
            wifiPanel.add(wifiSSID);
            JLabel passLabel = new JLabel("Password");
            wifiPanel.add(passLabel);
            wifiPassword = new JTextField("1928374655");
            wifiPanel.add(wifiPassword);
            SpringUtilities.makeGrid(wifiPanel, 2, 2, 5, 5, 5, 5);
            JButton saveWifiButton = new JButton("Save Wifi Conf.");
            saveWifiButton.setSize(300, 30);
            saveWifiButton.setBounds(10, 440, 300, 30);
            saveWifiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        String message = "ServiceSTA:"+wifiSSID.getText().toString()+"#"+wifiPassword.getText().toString();
                        System.out.println("Mes:"+message);
                        SocketLibrary.SocketClient.send_answer_to_client(screenIpAddress.getText(), 2750, message, 10000, null);
                    }
                    catch(Exception ex){
                        
                    }
                }
            });
            

            MainFrame.getLayeredPane().add(testPanel);
            MainFrame.getLayeredPane().add(sendButton);
            MainFrame.getLayeredPane().add(ipPanel);
            MainFrame.getLayeredPane().add(saveIpButton);
            MainFrame.getLayeredPane().add(wifiPanel);
            MainFrame.getLayeredPane().add(saveWifiButton);
            MainFrame.setVisible(true);
        }
        catch(Exception e){
            System.err.println("Exception:"+e.getMessage());
        }
    }
    
}
