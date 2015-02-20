/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step4bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author e.h.juerrens@52north.org
 *
 */
public class Step4bTest {

	private static final Logger logger = LoggerFactory.getLogger(Step4bTest.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (logger.isTraceEnabled()) {
			logger.trace("main()");
		}
		final MainController f = MainController.getInstance();
//		Lang.setCurrentLocale(Locale.GERMAN);
		final int firstLineWithData = 1;
		int i = 0;
		final FeatureOfInterest foi = TestData.EXAMPLE_FOI;
		final TableController tc = TableController.getInstance();
		tc.setContent(TestData.EXAMPLE_TABLE);
		tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
		tc.setColumnHeading(++i, Lang.l().sensor());
		tc.setColumnHeading(++i, Lang.l().observedProperty());
		tc.setColumnHeading(++i, Lang.l().featureOfInterest());
		tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
		tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
		tc.setColumnHeading(++i, Lang.l().featureOfInterest());
		tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
		tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());

		final ModelStore ms = ModelStore.getInstance();
		final NumericValue nV1 = new NumericValue(), nV2 = new NumericValue();
		nV1.setTableElement(new Column(5, firstLineWithData));
		nV2.setTableElement(new Column(8, firstLineWithData));

		ms.add(nV1);
		ms.add(nV2);

		f.setStepController(
				new Step4bController(
						new Step4bModel(foi,firstLineWithData),firstLineWithData));
	}
}
