package de.fzj.atlascore.region;

import de.fzj.atlascore.region.entity.Region;
import de.fzj.atlascore.region.entity.RegionBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RegionRepository {

    private HashMap<String, Set<String>> refSpaceRegions = new HashMap<>();

    public List<Region> findAllByReferencespaceAndParcellation(String refSpace, String parcellation) {
        if(!refSpaceRegions.containsKey(refSpace)) {
            refSpaceRegions.put(refSpace, new HashSet<>());
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new ClassPathResource(
                                        String.format("data/brains/%s.json", refSpace),
                                        this.getClass().getClassLoader()).getInputStream()
                        )
                );
                String fileAsString = reader.lines().collect(Collectors.joining(" "));
                JSONObject jsonObject = new JSONObject(fileAsString);
                JSONArray jsonArray = jsonObject.getJSONArray("parcellations");
                for (Object o : jsonArray) {
                    if (((JSONObject) o).get("name").toString().equals(parcellation)) {
                        for (int i = 0; i < ((JSONObject) o).getJSONArray("regions").length(); i++) {
                            getRegionTMP(refSpace, ((JSONObject) o).getJSONArray("regions").getJSONObject(i));
                        }
                    }
                }
            } catch (IOException e) {
                //TODO Log error
            }
        }
        List<Region> regions = new LinkedList<>();
        for(String region : refSpaceRegions.get(refSpace)) {
            regions.add(RegionBuilder.aRegion().withName(region).build());
        };
        return regions;
    }

    private void getRegionTMP(String refSpace, JSONObject jsonObject) {
        JSONArray children = jsonObject.getJSONArray("children");
        if(children.isEmpty()) {
            refSpaceRegions.get(refSpace).add(jsonObject.get("name").toString().split("-")[0]);
        } else {
            for(Object o : children) {
                getRegionTMP(refSpace, (JSONObject) o);
            }
        }
    }

    public Region findOneByReferencespaceAndParcellationAndName(String refSpace, String parcellation, String name) {
        if(!refSpaceRegions.containsKey(refSpace)) {
            findAllByReferencespaceAndParcellation(refSpace, parcellation);
        }
        String s = refSpaceRegions.get(refSpace).stream().filter(region -> region.equals(name)).findFirst().get();
        if(s != null) {
            return RegionBuilder.aRegion().withName(s).build();
        } else {
            return null;
        }
    }
}