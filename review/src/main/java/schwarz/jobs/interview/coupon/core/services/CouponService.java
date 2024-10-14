package schwarz.jobs.interview.coupon.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Optional<Basket> apply(final Basket basket, final String code) {
        return getCoupon(code).map(coupon -> {
            // simplified code and excess if removed
            if (!(basket.getValue().doubleValue() >= 0)) {
                System.out.println("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");
                throw new RuntimeException("Can't apply negative discounts");
            }

            if (basket.getValue().doubleValue() > 0) basket.applyDiscount(coupon.getDiscount());

            return basket;
        });
    }

    public Coupon createCoupon(final CouponDTO couponDTO) {
        // code changed to avoid saving null coupon (and causing an exception to be thrown), if not saved it returns null
        try {
            Coupon coupon = Coupon
                    .builder()
                    .code(couponDTO.getCode().toLowerCase())
                    .discount(couponDTO.getDiscount())
                    .minBasketValue(couponDTO.getMinBasketValue())
                    .build();

            return couponRepository.save(coupon);
        } catch (Exception ignored) {
        }

        return null;
    }

    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {
        // changed to use stream (lambda), in addition to avoiding errors when using "get" for an empty optional.
        // If no coupon is found, an empty list is returned
        return couponRequestDTO.getCodes()
                .stream()
                .map(code -> couponRepository.findByCode(code).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
