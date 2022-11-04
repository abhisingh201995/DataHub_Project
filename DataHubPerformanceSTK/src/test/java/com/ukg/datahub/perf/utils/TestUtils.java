/**************************************************************************
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2019 Adobe Systems Incorporated All Rights Reserved.
 * NOTICE: All information contained herein is, and remains the property of Adobe Systems
 * Incorporated and its suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary to Adobe Systems Incorporated and its suppliers and are protected by all
 * applicable intellectual property laws, including trade secret and copyright laws. Dissemination
 * of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Adobe Systems Incorporated.
 *
 * @author Vinay Kumar Sharma
 **************************************************************************/

package com.ukg.datahub.perf.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class TestUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Method to check for numeric parameters
     *
     * @param parameterName
     * @param parameterValue
     * @return
     */
    public static Boolean checkForValidNumericParameter(String parameterName, String parameterValue) {
        if (StringUtils.isBlank(parameterValue))
            throw new IllegalArgumentException(parameterName + " cannot be blank ");
        if (!StringUtils.isNumeric(parameterValue))
            throw new IllegalArgumentException("Please input a valid " + parameterName);
        return true;
    }

    public static void validateClassData(Object data) {
        Set<ConstraintViolation<Object>> errors = validator.validate(data);
        if (CollectionUtils.isNotEmpty(errors)) {
            throw new ConstraintViolationException(errors);
        }
    }
}
