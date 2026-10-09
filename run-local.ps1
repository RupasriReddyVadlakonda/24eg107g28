$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '.env'
if (-not (Test-Path $envFile)) {
    throw 'Missing .env. Copy .env.example to .env and replace all placeholders first.'
}

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith('#')) {
        $separator = $line.IndexOf('=')
        if ($separator -gt 0) {
            $name = $line.Substring(0, $separator).Trim()
            $value = $line.Substring($separator + 1).Trim()
            [Environment]::SetEnvironmentVariable($name, $value, 'Process')
            if ($name -eq 'DB_URL') {
                [Environment]::SetEnvironmentVariable('SPRING_DATASOURCE_URL', $value, 'Process')
            } elseif ($name -eq 'DB_USERNAME') {
                [Environment]::SetEnvironmentVariable('SPRING_DATASOURCE_USERNAME', $value, 'Process')
            } elseif ($name -eq 'DB_PASSWORD') {
                [Environment]::SetEnvironmentVariable('SPRING_DATASOURCE_PASSWORD', $value, 'Process')
            }
        }
    }
}

Push-Location (Join-Path $PSScriptRoot 'backend')
try {
    .\mvnw.cmd spring-boot:run
} finally {
    Pop-Location
}
