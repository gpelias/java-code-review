package schwarz.jobs.interview.coupon.core.services.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Basket {

    @NotNull
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private boolean applicationSuccessful;

    // in this application it was always false, and I changed it to true, in this case an exception will never be thrown
    public void applyDiscount(final BigDecimal discount) {
        this.appliedDiscount = discount;
        this.applicationSuccessful = true;
    }

}
