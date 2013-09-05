package pt.webdetails.cpf.repository.api;

/**
 * Minimal repository access provider for basic plugin needs 
 */
public interface IRepositoryAccessFactory {

  /**
   * @param basePath (optional) all subsequent paths will be relative to this
   * @return {@link IUserContentAccess} for user repository access
   */
  IUserContentAccess getUserContentAccess(String basePath);

  /**
   * @param basePath (optional) base path relative to plugin's folder.
   *                 all subsequent paths will be relative to it
   * @return {@link IReadAccess} for files in plugin's own folder
   */
  IReadAccess getPluginResourceAccess(String basePath);

  /**
   * @param basePath (optional) base path relative to plugin's repository folder.
   *                 all subsequent paths will be relative to it
   * @return {@link IReadAccess} for files in plugin's own folder
   */
  IReadAccess getPluginRepositoryResourceAccess(String basePath);

//  /**
//   * @param basePath (optional) base path relative to plugin's repository folder.
//   *                 all subsequent paths will be relative to it
//   * @return {@link IPluginResourceAccess} for files in plugin's own folder
//   */
//  public IPluginResourceRWAccess getPluginResourceRWAccess(String basePath);
//  //TODO: a legacy PluginResource/Solution accessor that checks the path for system?..
//  // and multiplexes to the appropriate class; maybe deprecate it at birth

//  /**
//   * Multiplexes legacy paths to appropriate resource access
//   * @param basePath
//   * @return
//   */
//  IPluginResourceAccess getLegacyResourceAccess(String basePath);
//
//  /**
//   * base path = system/plugin
//   * @return {@link IPluginResourceAccess} for current plugin.
//   */
//  IPluginResourceAccess getPluginResourceAccess();
//
//  /**
//   * base path = system/plugin
//   * @return {@link IPluginResourceAccess} for specified plugin.
//   */
//  IPluginResourceAccess getPluginResourceAccess(String plugin);
//
//  //TODO: friggin long name
//  /**
//   * @param path relative to repository plugin folder.
//   * @return {@link IPluginResourceAccess} for solution
//   */
//  IPluginResourceAccess getPluginRepositoryResourceAccess(String path);
}
