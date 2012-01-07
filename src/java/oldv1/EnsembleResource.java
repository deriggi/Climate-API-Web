/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

/**
 *
 * @author wb385924
 */
import ascii.tnc.TNCEnsembleRequestBuilder;
import com.google.gson.Gson;
import domain.DerivativeStats;
import domain.UnionedMapPart;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/ensemble")
public class EnsembleResource {

    @GET
    @Path("/{stat}/{scenario}/{timeperiod}/{iso}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEnsembles(@PathParam("iso") String iso, @PathParam("stat") String stat, @PathParam("scenario") String scenario, @PathParam("timeperiod") String timePeriod) {
        DerivativeStats.climatestat climstat = DerivativeStats.getInstance().getClimateStat(stat);
        DerivativeStats.scenario scen = DerivativeStats.getInstance().getScenario(scenario);
        DerivativeStats.time_period tp = DerivativeStats.time_period.valueOf(timePeriod);
        TNCEnsembleRequestBuilder builder = TNCEnsembleRequestBuilder.get();
        String requestName = builder.buildRequestName(iso, 0, climstat, scen, tp);
        List<UnionedMapPart> mapParts = builder.collectGeometry(4, requestName);

        return new Gson().toJson(mapParts);

    }
}
