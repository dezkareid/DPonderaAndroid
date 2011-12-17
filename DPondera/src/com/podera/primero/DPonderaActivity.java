package com.podera.primero;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class DPonderaActivity extends Activity {
	
	EditText calificacion,creditos;
	TextView promedio;
	ListView lista_entradas;
	List<String> calificaciones,l_creditos,lista_visible;
	int sumatoria, factor_ponderacion,valor_calificacion,valor_creditos,seleccionado;
	ArrayAdapter <String>adaptador;
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setTitle("DPondera By DezkaReid");
        iniciar_componentes();
        agregaListener();
    }
   
	public void iniciar_componentes()
	{
		calificacion= (EditText) findViewById(R.id.campo_calificacion);
		creditos=(EditText) findViewById(R.id.campo_creditos);
		promedio=(TextView) findViewById(R.id.eti_promedio);
		lista_entradas=(ListView) findViewById(R.id.lista_entradas);
		calificaciones=new ArrayList<String>();
		lista_visible=new ArrayList<String>();
		l_creditos=new ArrayList<String>();
		reinicia_variables();
		adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista_visible);
		lista_entradas.setAdapter(adaptador);		
	}

	public void reinicia_variables()
	{
		sumatoria=0; 
		factor_ponderacion=0;	
		valor_calificacion=0;
		valor_creditos=0;
		seleccionado=-1;	
	}

	public boolean validacion()
	{
	
		return valida_longitud()&&valida_cantidad();

	}

	public boolean valida_longitud()
	{
		if(creditos.length()==0|calificacion.length()==0)
			return false;
		else
			return true;
	
	}

	public boolean valida_cantidad()
	{
		valor_calificacion=Integer.parseInt(calificacion.getText().toString());
		valor_creditos=Integer.parseInt(creditos.getText().toString());
		return valor_calificacion<11&valor_creditos>0;
	
	}

	public void pondera()
	{
		if(!validacion())
		{
			muestradialogocorto("Datos de entrada no validos");
			return;	
		}
		
		String item_calificacion=calificacion.getText().toString();
		String item_creditos=creditos.getText().toString();
		calificaciones.add(item_calificacion);
		l_creditos.add(item_creditos);
		lista_visible.add(item_calificacion+espaciado()+item_creditos);
		adaptador.notifyDataSetChanged();
		factor_ponderacion=factor_ponderacion+(valor_calificacion*valor_creditos);
		sumatoria=sumatoria+valor_creditos;
		actualizacion();
		calificacion.setText("");
		creditos.setText("");
	}

	public String espaciado()
	{
		if (valor_calificacion<10&&valor_creditos<10)
			return "       ";
		else
           if(valor_calificacion<10||valor_creditos<10)
               return "      ";
           else
               return "    ";
	}

	public void actualizacion()
	{
	   double f=factor_ponderacion/(sumatoria*1.0);
	   DecimalFormat formato=new DecimalFormat("##.##");
	   promedio.setText(formato.format(f));
	}

	public void agregaListener()
	{
		lista_entradas.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				seleccionado=position;
				muestradialogocorto("Se he seleccionado el elemento: "+String.valueOf(seleccionado));
			}
			});
	
	}

	public void click_eliminar(View v)
	{
		if(seleccionado!=-1)
		{
			muestraMensajeEliminar("¿Esta seguro que desea eliminar un elemento?").show();
		}
		else
		{
		muestradialogocorto("Selecciona alguna entrada");
		}	
	}

	public void click_agregar(View v){
	pondera();
	}

	public void click_reiniciar(View v)
	{
	muestraMensajeReiniciar("¿Esta seguro borrar todo?").show();
	}

	public void realiza_calculos()
	{
		factor_ponderacion=0;
		sumatoria=0;
		int tamaño=lista_visible.size();
		if(tamaño>0)
		{
			for(int i=0;i<tamaño;i++)
			{
				factor_ponderacion=factor_ponderacion+(convertir(calificaciones.get(i).toString())*convertir(l_creditos.get(i).toString()));
				sumatoria=sumatoria+convertir(l_creditos.get(i).toString());
			}
		actualizacion();
		}
		else
			promedio.setText("0");
	}

	public int convertir(String numero)
	{
	return Integer.parseInt(numero);
	}

	public void muestradialogocorto(String mensaje)
	{
		Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
	
	}

	public AlertDialog muestraMensajeEliminar(String mensaje)
	{
		AlertDialog.Builder dialogo= new AlertDialog.Builder(this);
		dialogo.setTitle("Confirmación de acción");
		dialogo.setMessage(mensaje);
		dialogo.setCancelable(false);
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener()
		{   
			public void onClick(DialogInterface dialog, int which) {
				if(seleccionado!=-1)
				{
					lista_visible.remove(seleccionado);
					calificaciones.remove(seleccionado);
					l_creditos.remove(seleccionado);
					adaptador.notifyDataSetChanged();
					realiza_calculos();
					seleccionado=-1;
	    		}
				else
				{
	    		muestradialogocorto("Selecciona alguna entrada");
				}
	    	
	    }
		});
		dialogo.setNegativeButton("No", null);
		return dialogo.create();

	
	}

	public AlertDialog muestraMensajeReiniciar(String mensaje){
		AlertDialog.Builder dialogo= new AlertDialog.Builder(this);
		dialogo.setTitle("Confirmación de acción");
		dialogo.setMessage(mensaje);
		dialogo.setCancelable(false);
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() 
		{        
			public void onClick(DialogInterface dialog, int which) 
			{
				lista_visible.clear();
				calificacion.setText("");
				creditos.setText("");
				promedio.setText("0");
				adaptador.notifyDataSetChanged();
				lista_entradas.removeAllViewsInLayout();
				calificaciones.clear();
				l_creditos.clear();
				reinicia_variables();
			}
	  });
	  dialogo.setNegativeButton("No",null);
	  return dialogo.create();

	
	}

}