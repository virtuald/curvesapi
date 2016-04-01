/*
* Copyright (c) 2005, Graph Builder
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* * Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* * Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* * Neither the name of Graph Builder nor the names of its contributors may be
* used to endorse or promote products derived from this software without
* specific prior written permission.

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CurveDemo implements ActionListener {

	private EditCurvePanel editCurvePanel = new EditCurvePanel();
	private ControlPathPanel controlPathPanel = new ControlPathPanel();
	private JFrame frame = new JFrame("Curve Demo");
	private JDialog editDialog = new JDialog(frame, "Curve Options");

	private JMenuItem editCurveMenuItem = new JMenuItem("Edit Curves");
	private JMenuItem setFlatnessMenuItem = new JMenuItem("Set Flatness");
	private JCheckBoxMenuItem showControlPointsCheckBox = new JCheckBoxMenuItem("Show Control Points", controlPathPanel.getShowControlPoints());
	private JCheckBoxMenuItem showControlLineCheckBox = new JCheckBoxMenuItem("Show Control Line", controlPathPanel.getShowControlLine());
	private JCheckBoxMenuItem showPointNumbersCheckBox = new JCheckBoxMenuItem("Show Point Numbers", controlPathPanel.getShowPointNumbers());
	private JCheckBoxMenuItem showPointLocationsCheckBox = new JCheckBoxMenuItem("Show Point Locations", controlPathPanel.getShowPointLocations());
	private JCheckBoxMenuItem antialiasCurvesCheckBox = new JCheckBoxMenuItem("Anti-alias Curves", controlPathPanel.getAntialiasCurves());
	private JCheckBoxMenuItem showCurvesCheckBox = new JCheckBoxMenuItem("Show Curves", controlPathPanel.getShowCurves());

	public CurveDemo() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(500, 500);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});

		frame.setContentPane(controlPathPanel);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu controlPathMenu = new JMenu("Control-Path");

		controlPathMenu.add(showControlPointsCheckBox);
		controlPathMenu.add(showControlLineCheckBox);
		controlPathMenu.add(showPointNumbersCheckBox);
		controlPathMenu.add(showPointLocationsCheckBox);
		controlPathMenu.add(antialiasCurvesCheckBox);
		controlPathMenu.add(showCurvesCheckBox);
		controlPathMenu.add(setFlatnessMenuItem);
		controlPathMenu.add(editCurveMenuItem);

		menuBar.add(controlPathMenu);

		editDialog.setContentPane(editCurvePanel);
		editDialog.setSize(500, 300);
		editDialog.setLocation((d.width - editDialog.getWidth()) / 2, (d.height - editDialog.getHeight()) / 2);

		editCurveMenuItem.addActionListener(this);
		setFlatnessMenuItem.addActionListener(this);
		showControlPointsCheckBox.addActionListener(this);
		showControlLineCheckBox.addActionListener(this);
		showPointNumbersCheckBox.addActionListener(this);
		showPointLocationsCheckBox.addActionListener(this);
		antialiasCurvesCheckBox.addActionListener(this);
		showCurvesCheckBox.addActionListener(this);

		frame.show();
	}

	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();

		if (src == editCurveMenuItem) {
			editCurvePanel.setControlPath(controlPathPanel.getControlPath());
			editCurvePanel.setRepaintPanel(controlPathPanel);
			editDialog.show();
		}
		else if (src == showControlPointsCheckBox) {
			controlPathPanel.setShowControlPoints(showControlPointsCheckBox.isSelected());
		}
		else if (src == showControlLineCheckBox) {
			controlPathPanel.setShowControlLine(showControlLineCheckBox.isSelected());
		}
		else if (src == showPointNumbersCheckBox) {
			controlPathPanel.setShowPointNumbers(showPointNumbersCheckBox.isSelected());
		}
		else if (src == showPointLocationsCheckBox) {
			controlPathPanel.setShowPointLocations(showPointLocationsCheckBox.isSelected());
		}
		else if (src == antialiasCurvesCheckBox) {
			controlPathPanel.setAntialiasCurves(antialiasCurvesCheckBox.isSelected());
		}
		else if (src == showCurvesCheckBox) {
			controlPathPanel.setShowCurves(showCurvesCheckBox.isSelected());
		}
		else if (src == setFlatnessMenuItem) {
			double f = controlPathPanel.getFlatness();

			Object o = JOptionPane.showInputDialog(frame, "Enter flatness value:", "Flatness", JOptionPane.QUESTION_MESSAGE, null, null, String.valueOf(f));

			if (o != null) {
				try {
					f = Double.parseDouble(o.toString());
					controlPathPanel.setFlatness(f);
				} catch (Throwable err) {
					JOptionPane.showMessageDialog(frame, err, "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		controlPathPanel.repaint();
	}

	public static void main(String[] args) {
		new CurveDemo();
	}
}
