package com.biermacht.brews.utils;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;

import java.util.ArrayList;

public class BrewCalculator {

  /**
   * Beer details calculations http://www.howtobrew.com/section1/chapter5-5.html
   * http://homebrew.stackexchange.com/questions/1434/wiki-how-do-you-calculate-original-gravity
   * <p/>
   * MCU = (weight of grain in lbs)*(color of grain in lovibond) / (volume in gal) SRM = 1.4922 *
   * MCU**.6859
   * <p/>
   * OG = (Weight of grain in lbs)*(Extract potential of grain)*(Extraction Efficiency)
   * <p/>
   * IBU = 7498*(Weight of hops in OZ)*(% of Alpha Acids)*(Utilization factor) / (Volume in gal)
   */

  public static float Color(Recipe r) {
    float SRM = 0;
    float MCU = 0;
    ArrayList<Ingredient> ingredientsList = r.getIngredientList();

    for (Ingredient i : ingredientsList) {
      if (i.getType().equals(Ingredient.FERMENTABLE)) {
        Fermentable g = (Fermentable) i;
        MCU += Units.kilosToPounds(g.getBeerXmlStandardAmount()) * g.getLovibondColor() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
      }
    }
    SRM = (float) (1.4922 * Math.pow(MCU, .6859));
    return SRM;
  }

  public static double GrainPercent(Recipe r, Ingredient i) {
    if (i.getType().equals(Ingredient.FERMENTABLE)) {
      double g_amt = Units.kilosToPounds(i.getBeerXmlStandardAmount());
      double tot_amt = TotalFermentableWeight(r);

      return (g_amt / tot_amt) * 100;
    }

    return 0;
  }

  public static double TotalFermentableWeight(Recipe r) {
    double amt = 0;
    for (Ingredient i : r.getIngredientList()) {
      if (i.getType().equals(Ingredient.FERMENTABLE)) {
        amt += Units.kilosToPounds(i.getBeerXmlStandardAmount());
      }
    }

    return amt;
  }

  public static double TotalBeerXmlMashWeight(Recipe r) {
    double amt = 0;
    for (Ingredient i : r.getIngredientList()) {
      if (i.getUse().equals(Ingredient.USE_MASH)) {
        amt += i.getBeerXmlStandardAmount();
      }
    }

    return amt;
  }

  // Returns original gravity, including fermentable and non fermentable sugars
  public static double OriginalGravity(Recipe r) {
    double pts = OriginalFermentableGravityPoints(r) + NonFermentableGravityPoints(r);

    return 1 + pts / 1000;
  }

  // Calculates contribution of fermentable sugars to gravity
  public static double OriginalFermentableGravityPoints(Recipe r) {
    float gravity_points = 0;
    ArrayList<Ingredient> ingredientsList = r.getIngredientList();

    for (Ingredient i : ingredientsList) {
      gravity_points += FermentableGravityPoints(r, i);
    }
    return gravity_points;
  }

  // Calculates contribution of non fermentable sugars to gravity
  public static double NonFermentableGravityPoints(Recipe r) {
    float gravity_points = 0;
    ArrayList<Ingredient> ingredientsList = r.getIngredientList();

    for (Ingredient i : ingredientsList) {
      gravity_points += NonFermentableGravityPoints(r, i);
    }
    return gravity_points;
  }

  // Returns the boil gravity for the given recipe.
  public static double BoilGravity(Recipe r) {
    if (r.getType().equals(Recipe.EXTRACT)) {
      return ExtractBoilGravity(r);
    }
    else {
      return AllGrainBoilGravity(r);
    }
  }

  public static double ExtractBoilGravity(Recipe r) {
    // Because this is used for hop utilization calculation,
    // We want to adjust this based on late extract additions

    double mGPs = OriginalGravity(r) - 1; //milliGPs
    double avgBoilTime = 0;
    int t = 0;

    // TODO: This is imprecise as it doesn't take into account
    // how much extract is used as a late addition.. Its is only an approximate
    for (Fermentable f : r.getFermentablesList()) {
      if (! f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
        t++;
        avgBoilTime += f.getTime();
      }
    }
    if (t != 0) {
      avgBoilTime = avgBoilTime / t;
    }
    else {
      avgBoilTime = r.getBoilTime();
    }

    return 1 + (mGPs * Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / Units.litersToGallons(r.getBeerXmlStandardBoilSize())) * (avgBoilTime / r.getBoilTime());
  }

  public static double AllGrainBoilGravity(Recipe r) {
    double mGPs = OriginalGravity(r) - 1;
    return 1 + (mGPs * Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / Units.litersToGallons(r.getBeerXmlStandardBoilSize()));
  }

  public static double FermentableGravityPoints(Recipe r, Ingredient i) {
    double pts = 0;
    if (i.getType().equals(Ingredient.FERMENTABLE)) {
      Fermentable f = (Fermentable) i;

      if (f.getFermentableType().equals(Fermentable.TYPE_EXTRACT)) {
        pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
      }
      else if (f.getFermentableType().equals(Fermentable.TYPE_SUGAR)) {
        pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
      }
      else if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
        if (r.getType().equals(Recipe.EXTRACT)) {
          pts = 5 * Units.kilosToPounds(f.getBeerXmlStandardAmount()) / Units.litersToGallons(r.getBeerXmlStandardBatchSize());

          if (f.getName().equalsIgnoreCase("Roasted Barley")) {
            pts = 10 * f.getDisplayAmount() / r.getDisplayBatchSize();
          }
          if (f.getName().equalsIgnoreCase("Black Patent Malt")) {
            pts = 10 * f.getDisplayAmount() / r.getDisplayBatchSize();
          }
        }
        else {
          // Either a partial mash or all grain recipe
          pts = r.getEfficiency() * Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / 100;
        }
      }
      else {
        pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
      }
    }
    return pts;
  }

  // Returns the number of non fermentable gravity points for the given ingredient
  public static double NonFermentableGravityPoints(Recipe r, Ingredient i) {
    if (i.getName().equalsIgnoreCase("Malto-Dextrine") || i.getName().equalsIgnoreCase("Dextrine Malt")) {
      return Units.kilosToPounds(i.getBeerXmlStandardAmount()) * 40 / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
    }

    else {
      return 0;
    }
  }

  public static double Bitterness(Recipe r) {
    double ibu = 0;

    ArrayList<Ingredient> ingredientsList = r.getIngredientList();

    // http://www.howtobrew.com/section1/chapter5-5.html
    for (Ingredient i : ingredientsList) {
      ibu += Bitterness(r, i);
    }

    return ibu;
  }

  // Returns the given bitterness in IBUs
  public static double Bitterness(Recipe r, Ingredient i) {
    double ibu = 0;
    double AAU = 0;
    double utilization = 0;

    if (i.getType().equals(Ingredient.HOP)) {
      Hop h = (Hop) i;
      // Only add bitterness if we are boiling the hops!
      if (h.getUse().equals(Hop.USE_BOIL)) {
        utilization = HopUtilization(r, h);
        AAU = Units.kilosToOunces(h.getBeerXmlStandardAmount()) * h.getAlphaAcidContent();
        ibu = (AAU * utilization * 75) / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
      }
    }

    return ibu;
  }

  public static double FinalGravity(Recipe r) {
    double pts = FinalFermentableGravityPoints(r) + NonFermentableGravityPoints(r);
    return 1 + pts / 1000;
  }

  public static double FinalFermentableGravityPoints(Recipe r) {
    double pts = OriginalFermentableGravityPoints(r);
    double attn;          // Yeast attentuation

    for (Ingredient i : r.getIngredientList()) {
      if (i.getType().equals(Ingredient.YEAST)) {
        // First, try to determine if we have an attenuation value
        Yeast y = (Yeast) i;
        attn = y.getAttenuation();

        if (attn > 0) {
          return pts * (100 - attn) / 100 - 1;
        }
      }
    }

    // If we don't have any yeast, there's no fermentation!
    return OriginalFermentableGravityPoints(r);
  }

  public static double AlcoholByVolume(Recipe r) {
    return AlcoholByVolume(r.getOG(), r.getFG());
  }

  public static double AlcoholByVolume(double og, double fg) {
    return (og - fg) * 131;
  }

  /**
   * @param og
   *         Original gravity (SG)
   * @param fg
   *         Final gravity (SG)
   * @return Apparent attenuation as a percentage out of 100
   */
  public static double Attenuation(double og, double fg) {
    return 100 * ((og - fg) / (og - 1));
  }

  public static double HopUtilization(Recipe r, Hop i) {
    float utilization;
    double bignessFactor;
    double boilTimeFactor;
    double boilGrav = BoilGravity(r);

    // Default : 1.65/4.25
    bignessFactor = 1.65 * Math.pow(.00025, boilGrav - 1);
    boilTimeFactor = (1 - Math.pow(Math.E, - .04 * i.getTime())) / 4.25;

    utilization = (float) (bignessFactor * boilTimeFactor);

    // Pellets require a little bit more hops.
    if (i.getForm().equals(Hop.FORM_PELLET)) {
      return .94 * utilization;
    }
    else {
      return utilization;
    }
  }

  public static double calculateStrikeTemp(double r,
                                           double T1,
                                           double T2) {
    /**
     * From Palmer:
     *   Tw = (.2/r)(T2 - T1) + T2
     *   Wa = (T2 - T1)(.2G + Wm)/(Tw - T2)
     *
     *   r = The ratio of water to grain in quarts per pound.
     *   Wa = The amount of boiling water added (in quarts).
     *   Wm = The total amount of water in the mash (in quarts).
     *   T1 = The initial temperature (¡F) of the mash.
     *   T2 = The target temperature (¡F) of the mash.
     *   Tw = The actual temperature (¡F) of the infusion water.
     *   G = The amount of grain in the mash (in pounds).
     *
     *   NOTE: All temperatures in degrees F.
     */
    return ((.2 / r) * (T2 - T1)) + T2;
  }

  /**
   * Adjusts a gravity reading based on temperature
   *
   * @param mg
   *         - Measured gravity
   * @param temp
   *         - Measured temperature (F)
   * @param ct
   *         - Temperature to which to calibrate (F)
   * @return - Calibrated gravity
   */
  public static double adjustGravityForTemp(double mg, double temp, double ct) {
    double cg; // Corrected grav
    cg = mg * (1.00130346 - 0.000134722124 * temp + 0.00000204052596 * temp * temp - 0.00000000232820948 * temp * temp * temp);
    cg = cg / (1.00130346 - 0.000134722124 * ct + 0.00000204052596 * ct * ct - 0.00000000232820948 * ct * ct * ct);

    return cg;
  }

}
