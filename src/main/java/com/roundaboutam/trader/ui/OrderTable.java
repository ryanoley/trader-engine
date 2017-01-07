package com.roundaboutam.trader.ui;

import javax.swing.table.*;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrderTable extends JTable implements MouseListener {

	private final transient TraderApplication application;

    public OrderTable(OrderTableModel orderTableModel, TraderApplication application) {
        super(orderTableModel);
        this.application = application;
        addMouseListener(this);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

    	Order order = ((OrderTableModel) dataModel).getOrder(row);

        int open = order.getOpen();
        int executed = order.getExecuted();
        boolean rejected = order.isRejected();
        boolean canceled = order.isCanceled();

        DefaultTableCellRenderer r = (DefaultTableCellRenderer) renderer;
        r.setForeground(Color.black);

        if (rejected)
            r.setBackground(Color.red);
        else if (canceled)
            r.setBackground(Color.white);
        else if (open == 0 && executed == 0)
            r.setBackground(Color.yellow);
        else if (open > 0)
            r.setBackground(Color.green);
        else if (open == 0)
            r.setBackground(Color.white);

        return super.prepareRenderer(renderer, row, column);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2)
            return;
        int row = rowAtPoint(e.getPoint());
        Order order = ((OrderTableModel) dataModel).getOrder(row);
        CancelOrder cancelOrder = new CancelOrder(order);
        application.cancel(cancelOrder);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}
