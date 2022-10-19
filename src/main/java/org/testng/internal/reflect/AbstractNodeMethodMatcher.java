package org.testng.internal.reflect;

import org.testng.internal.ThreeStateBoolean;

import java.util.List;
import java.util.Set;

/**
 * Created on 1/4/16.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public abstract class AbstractNodeMethodMatcher extends AbstractMethodMatcher {

  private Parameter[] conformingParameters = null;
  private Set<InjectableParameter> conformingInjects = null;

  public AbstractNodeMethodMatcher(final MethodMatcherContext context) {
    super(context);
  }

  protected Parameter[] getConformingParameters() {
    return conformingParameters;
  }

  protected Set<InjectableParameter> getConformingInjects() {
    return conformingInjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean hasConformance() {
    boolean matching = false;
    for (final Set<InjectableParameter> injects : getConformanceInjectsOrder()) {
      final Parameter[] parameters = ReflectionRecipes.filter(getContext().getMethodParameter(), injects);
      matching = match(parameters, getContext().getArguments());
      if (matching) {
        conformingParameters = parameters;
        conformingInjects = injects;
        break;
      }
    }
    return matching;
  }

  /**
   * @return injects to check against.
   */
  protected abstract List<Set<InjectableParameter>> getConformanceInjectsOrder();

  /**
   * Checks if its possible to gives an array consumable by java method invoker.
   *
   * @param parameters array of parameter instances under question.
   * @param arguments  instances to be verified.
   * @return matches or not
   */
  protected abstract boolean match(final Parameter[] parameters, final Object[] arguments);

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getConformingArguments() throws MethodMatcherException {
    if (ThreeStateBoolean.NONE.equals(getConforms())) {
      conforms();
    }
    if (getConformingParameters() == null) {
      throw new MethodMatcherException(this.getClass().getSimpleName() + " mismatch", getContext().getMethod(),
        getContext().getArguments());
    }

    return ReflectionRecipes.inject(
      getContext().getMethodParameter(),
      InjectableParameter.Assistant.ALL_INJECTS,
      matchingArguments(getConformingParameters(), getContext().getArguments()),
      getContext().getMethod(),
      getContext().getTestContext(),
      getContext().getTestResult()
    );
  }

  /**
   * If possible gives an array consumable by java method invoker.
   *
   * @param parameters array of parameter instances under question.
   * @param arguments  instances to conform.
   * @return conforming argument array
   */
  protected abstract Object[] matchingArguments(final Parameter[] parameters, final Object[] arguments);
}
