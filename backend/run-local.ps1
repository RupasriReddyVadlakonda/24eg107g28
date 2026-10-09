$ErrorActionPreference = 'Stop'
$launcher = Join-Path (Split-Path -Parent $PSScriptRoot) 'run-local.ps1'

if (-not (Test-Path $launcher)) {
    throw "Local launcher not found: $launcher"
}

& $launcher
exit $LASTEXITCODE
