/*----------------------------------------------------------------------------*/
/* Copyright (c) 2015-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj2.filters;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Superclass for filters.
 *
 * @deprecated This class is no longer used.
 */
@Deprecated(since = "2020", forRemoval = true)
public abstract class Filter implements PIDSource {
  private final PIDSource m_source;

  public Filter(PIDSource source) {
    m_source = source;
  }

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {
    m_source.setPIDSourceType(pidSource);
  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return m_source.getPIDSourceType();
  }

  @Override
  public abstract double pidGet();

  /**
   * Returns the current filter estimate without also inserting new data as pidGet() would do.
   *
   * @return The current filter estimate
   */
  public abstract double get();

  /**
   * Reset the filter state.
   */
  public abstract void reset();

  /**
   * Calls PIDGet() of source.
   *
   * @return Current value of source
   */
  protected double pidGetSource() {
    return m_source.pidGet();
  }
}
