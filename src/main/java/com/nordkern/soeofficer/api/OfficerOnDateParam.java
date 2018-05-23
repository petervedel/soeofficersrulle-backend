package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OfficerOnDateParam {

    @Setter
    @Getter
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value = TemporalType.DATE)
    private Date date;
}
