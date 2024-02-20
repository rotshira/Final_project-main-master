package Jamming;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Roi on 9/23/2016.
 */
public class ClientList {

    private List<Client> Clients;
    public void Init(double x1, double y1, double x2, double y2)
    {
        Random R1 = new Random();
        Clients = new ArrayList<>();
        for (int i=0; i< Utils.NumberOfClient ; i++) {
            Client tmp = new Client(x1, y1, x2, y2, R1);
            Clients.add(tmp);
        }

    }

    public List<Client> getClients() {
        return Clients;
    }

    public void movebyCOGSOG()
    {
        Random R1 = new Random();
        for (int i=0; i< Utils.NumberOfClient; i++) {
            this.Clients.get(i).moveByCOGSOG(R1);
        }
    }


    public void PrintClinets()
    {
        for(int i=0; i<Utils.NumberOfClient; i++)
            this.Clients.get(i).PrintClient(i);
    }

    public void senseNoise(JammerParticle jammer)
    {
        for (int i=0; i< Utils.NumberOfClient; i++) {
            this.Clients.get(i).senseNoise(jammer);
        }
    }

    public void senseNoise(List<JammerParticle> realJammerList) {
        for (int i=0; i< Utils.NumberOfClient; i++) {
            this.Clients.get(i).senseNoise(realJammerList);
        }
    }

    public void senseNoiseUnknownJammerPower(List<JammerParticle> realJammerList) {
        for (int i=0; i< Utils.NumberOfClient; i++) {
            this.Clients.get(i).senseNoiseUnknownJammerPower(realJammerList);
        }
    }
}
