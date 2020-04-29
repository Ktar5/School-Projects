

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {
    JTextArea area = new JTextArea();
    JTextField field = new JTextField();
    JButton add = new JButton("Add");

    public View() {
        this.setPreferredSize(new Dimension(250, 250));

        area.setPreferredSize(new Dimension(200, 100));

        //input
        field.setPreferredSize(new Dimension(200, 50));

        add.setPreferredSize(new Dimension(200, 25));
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MVCTester.controller.addNewLine(field.getText());
            }
        });

        this.setLayout(new FlowLayout());

        this.add(add);
        this.add(area);
        this.add(field);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void update(Model model){
        StringBuilder builder = new StringBuilder();
        for (String s : model.getLinesOfText()) {
            builder.append(s).append("\n");
        }
        area.setText(builder.toString());
        field.setText("");
    }
}
