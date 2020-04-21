package de.fzj.atlascore.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceCommunicator {

    public CellDensities getCellDensities(List<Mask> masks, MaskCombination maskCombination) {
        /**
         * Call ImageService with a list of masks:
         * Mask defined by:
         * {
         *   "parcellationId" : "string",
         *   "regionId" : "string",
         * ? "url" : "string"
         * }
         */

        return new CellDensities(
                masks,
                maskCombination, 237.56355746025292,
                24.582238428256325,
                255,
                255,
                13655596,
                createDistributionData()
        );
    }

    private DistributionData[] createDistributionData() {
        return new DistributionData[]{
                new DistributionData(0,0),
                new DistributionData(1,0),
                new DistributionData(2,1),
                new DistributionData(3,5),
                new DistributionData(4,11),
                new DistributionData(5,17),
                new DistributionData(6,20),
                new DistributionData(7,34),
                new DistributionData(8,23),
                new DistributionData(128,200),
                new DistributionData(200,324),
                new DistributionData(255,99)
        };
    }
}