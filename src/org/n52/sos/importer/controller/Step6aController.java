package org.n52.sos.importer.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.bean.FeatureOfInterest;
import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.ObservedProperty;
import org.n52.sos.importer.bean.Resource;
import org.n52.sos.importer.bean.SensorName;
import org.n52.sos.importer.bean.Store;
import org.n52.sos.importer.bean.UnitOfMeasurement;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.view.Step6aPanel;

public class Step6aController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6aController.class);
	
	private Step6aPanel step6aView;
	
	private Step6aModel step6aModel;
	
	private TableController tableController = TableController.getInstance();
	
	public Step6aController(Step6aModel step6aModel) {
		this.step6aModel = step6aModel;
		Resource resource = step6aModel.getResource();
		resource.unassignFromMeasuredValues();
		String questionText = "New " + resource.toString();	//TODO
		String markingText = "Mark all measured value columns where the " + resource + " corresponds to:";
		
		step6aView = new Step6aPanel(questionText, markingText);
		step6aView.setResourceName(resource.getName());
		//step6aView.setResourceURI(resource.getURI().toString());
		
		tableController.allowColumnSelection();
		tableController.addMultipleSelectionListener(new SelectionChanged());
		tableController.allowMultipleSelection();
		
		int[] selectedColumns = step6aModel.getSelectedColumns();
		for (int column: selectedColumns) 
			tableController.selectColumn(column);
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {		
		//get and check name
		String name = step6aView.getResourceName();
		if (name == null) {
			logger.error("No Name given.");
			return;
		}
		
		//get and check URI
		String uri = step6aView.getResourceURI();
		URI URI = null;
		try {
			URI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Wrong URI", e);
			return;
		}
		
		//get selected columns
		int[] columns = tableController.getSelectedColumns();
		if (columns.length == 0) {
			logger.warn("No Columns selected.");
			return;
		}
		step6aModel.setSelectedColumns(columns);
		
		//assign selected measured value columns or rows this type
		Resource resource = step6aModel.getResource();
		resource.setName(name);
		resource.setURI(URI);
		
		for (int c: columns) {
			MeasuredValue mv = Store.getInstance().getMeasuredValueAtColumn(c); 
			resource.assign(mv);
		}
		
		//check if there are any measured value rows or columns without this type
		//if yes, call the particular panel 
		//if no, go to the new step
		Resource r = Store.getInstance().getMissingResourceForMeasuredValues();
		
		if (r != null) {
			Step6aController step6aController = new Step6aController(new Step6aModel(r));
			MainController.getInstance().setStepController(step6aController);
		}
	}
	
	protected void loadSettings() {
		// TODO Auto-generated method stub
		// rows or columns
		// which rows or columns were selected
		// name 
		// uri
		
	}	
	
	private class SelectionChanged implements TableController.MultipleSelectionListener {

		@Override
		public void columnSelectionChanged(int[] selectedColumns) {
			
			for (int column: selectedColumns) {
				System.out.println(column);
				MeasuredValue mv = Store.getInstance().getMeasuredValueAtColumn(column);
				if (mv == null) {
					logger.error("This is not a measured value.");
					tableController.deselectColumn(column);
					return;
				} 
				
				Resource resource = step6aModel.getResource();
				if (resource.isAssigned(mv)) {
					logger.error(resource + " already set for this measured value.");
					tableController.deselectColumn(column);
				}
			}
		}

		@Override
		public void rowSelectionChanged(int[] selectedRows) {
			// TODO Auto-generated method stub
			
		}	
	}

	@Override
	public String getDescription() {
		return "Step 6a: Add missing Metadata";
	}

	@Override
	public JPanel getStepPanel() {
		return step6aView;
	}
}