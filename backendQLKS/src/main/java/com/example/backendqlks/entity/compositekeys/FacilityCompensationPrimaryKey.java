package com.example.backendqlks.entity.compositekeys;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class FacilityCompensationPrimaryKey implements Serializable {
    private int facilityId;
    private int invoiceDetailId;

    private FacilityCompensationPrimaryKey(int facilityId, int invoiceDetailId){
        this.facilityId = facilityId;
        this.invoiceDetailId = invoiceDetailId;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;

        if(obj instanceof  FacilityCompensationPrimaryKey other){
            return (this.facilityId == other.facilityId) &&
                        (this.invoiceDetailId ==other.invoiceDetailId);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(facilityId, invoiceDetailId);
    }
}
