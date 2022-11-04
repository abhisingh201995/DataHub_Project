<html>
    <body>
        <h3>Test Report Summary</h3>
        <table>
                <#if testId??>
        	<tr>
				<td class="col-md-1">
					<label><b>Test Id:</b></label>
				</td>
				<td class="col-md-2">
					${testId}
				</td>
			</tr>
			    </if>
			<#if testDesc??>
			<tr>
				<td class="col-md-1">
					<label><b>Test Description:</b></label>
				</td>
				<td class="col-md-2">
					<label>${testDesc}</label>
				</td>
			</tr>
			</#if>
			<#if dbUser??>
        	<tr>
				<td class="col-md-1">
					<label><b>DB USER:</b></label>
				</td>
				<td class="col-md-2">
					<label>${dbUser}</label>
				</td>
			</tr>
			</#if>
			<#if dbName??>
			<tr>
				<td class="col-md-1">
					<label><b>DB NAME:</b></label>
				</td>
				<td class="col-md-2">
					<label>${dbName}</label>
				</td>
			</tr>
			</#if>
			<#if gcpDataProject??>
				<tr>
					<td class="col-md-1">
						<label><b>GCP Data Project:</b></label>
					</td>
					<td class="col-md-2">
						<label>${gcpDataProject}</label>
					</td>
				</tr>
			</#if>
			<#if productLoginURL??>
				<tr>
					<td class="col-md-1">
						<label><b>Product Login URL:</b></label>
					</td>
					<td>
						<a href="${productLoginURL}">${productLoginURL}</a>
					</td>
				</tr>
			</#if>
			<tr>
				<td class="col-md-1">
					<label><b>Result</b></label>
				</td>
				<#if result == "FAILED">
				    <td class="col-md-2" bgcolor="red" >
					    <b>${result}</b>
				    </td>
				<#else>
				    <td class="col-md-2" bgcolor="green" >
				         <b>${result}</b>
                    </td>
				</#if>
			</tr>
			</#if>
			<#if testParameters?size!=0>
            				<tr>
            					<td class="col-md-1">
            						<label><b>Test Parameters:</b></label>
            					</td>
            					<td class="col-md-2">
            						<table style="border-style: double;">
            							<#list testParameters?keys as paramName>
            								<tr>
            									<td style="border-bottom-style: ridge; border-right-style: ridge;">
            										<label> <b> <font color="blue"> ${paramName}</font></b></label>
            									</td>
            									<td style="border-bottom-style: ridge;">
            										<#list testParameters[paramName]  as value>
            											 <#if value??>
            												<b> <font color="blue"> ${value} </font> <b>
            												<br>
            											 </#if>
            										</#list>
            									</td>
            								</tr>
            							</#list>
            						</table>
            					</td>
            				</tr>
            			</#if>

            <#if error??>
            				<tr>
            					<td class="col-md-1">
            						<label><b>ERROR MESSAGE:</b></label>
            					</td>
            					<td class="col-md-2" bgcolor="red">
            						  <b>${error}</b>
            					</td>
            				</tr>
           </#if>

			<#if stepOutputs??>
				<tr>
					<td>
						<label><b>Test Outputs:</b></label>
					</td>
					<td>
						<table style="border-style: double;">
							<#list stepOutputs?keys as name>
								<tr>
									<td style="border-bottom-style: ridge; border-right-style: ridge;">
										<label> <b> <br> <font color="green"> ${name} </font> </br> </b></label>
									</td>
									<#list stepOutputs[name] as value>
									<#if value??>
									<td style="border-bottom-style: ridge;">
									<b> <br> <font color="green"> ${value} </font> </br> </b>
									</td>
									</#if>
									</#list>
								</tr>
							</#list>
						</table>
					</td>
				</tr>
			</#if>
        </table>
		</body>
</html>