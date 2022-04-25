package com.paidyinc.forex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController {
    private static final Logger LOG = LoggerFactory.getLogger(ForexController.class);


    @GetMapping(value = "/fx/{fromFx}/{toFx}")
    @ResponseBody
    public ForexResult getForexResult(@PathVariable("fromFx") String fromFx, @PathVariable("toFx") String toFx) {
        LOG.debug("getting rate for FX Pair: [from {} >> to {}]", fromFx, toFx);

        return getForexRate(fromFx, toFx);
    }

    private ForexResult getForexRate(String fromFx, String toFx) {
        var forexRate = ForexResult.builder()
                .rate(123.45d)
                .build();

        LOG.debug("found rate for FX Pair [{} >> {}] = {}", fromFx, toFx, forexRate.getRate());

        return forexRate;
    }
}
